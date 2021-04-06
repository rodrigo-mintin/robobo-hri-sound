aspackage com.mytechia.robobo.framework.hri.sound.speechDetection.Vosk.TarsosDSP;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.mytechia.robobo.framework.LogLvl;
import com.mytechia.robobo.framework.hri.sound.soundDispatcherModule.ISoundDispatcherModule;

import org.kaldi.KaldiRecognizer;
import org.kaldi.RecognitionListener;
import org.kaldi.SpeechService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.HashSet;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFloatConverter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;

public class TarsosDSPSpeechService {

    /**
     * Creates speech service. Service holds the AudioRecord object, so you
     * need to call {@link release} in order to properly finalize it.
     *
     */

    private final KaldiRecognizer recognizer;
    private final int sampleRate;
    private final static float BUFFER_SIZE_SECONDS = 0.4f;
    private final ISoundDispatcherModule dispatcher;
    private int bufferSize;
    private final TarsosDSPAudioFormat voskFormat;
    private TarsosDSPAudioFloatConverter converter;
    private RecognizerProcessor processor = null;

    //private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Collection<RecognitionListener> listeners = new HashSet<RecognitionListener>();

    //private final AudioRecord recorder;

    /*
        ENCODING_PCM_16BIT: The audio sample is a 16 bit signed integer typically stored as a Java
        short in a short array, but when the short is stored in a ByteBuffer, it is native endian
        (as compared to the default Java big endian). The short has full range from [-32768, 32767],
        and is sometimes interpreted as fixed point Q.15 data.
     */

    public TarsosDSPSpeechService(KaldiRecognizer recognizer, float sampleRate, ISoundDispatcherModule dispatcherModule) throws IOException {
        //super(recognizer, sampleRate);
        voskFormat = new TarsosDSPAudioFormat(sampleRate, 16 ,1,true,true);
        converter = TarsosDSPAudioFloatConverter.getConverter(voskFormat);
        
        this.dispatcher = dispatcherModule;

        this.recognizer = recognizer;
        this.sampleRate = (int)sampleRate;

        /**
        recorder = new AudioRecord(
                AudioSource.VOICE_RECOGNITION, this.sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize * 2);
        **/
        bufferSize = Math.round(this.sampleRate * BUFFER_SIZE_SECONDS);

        // Never throws
    }

    /**
     * Adds listener.
     */
    public void addListener(RecognitionListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Removes listener.
     */
    public void removeListener(RecognitionListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }


    private abstract class RecognitionEvent implements Runnable {
        public void run() {
            RecognitionListener[] emptyArray = new RecognitionListener[0];
            for (RecognitionListener listener : listeners.toArray(emptyArray))
                execute(listener);
        }

        protected abstract void execute(RecognitionListener listener);
    }

    private class ResultEvent extends RecognitionEvent {
        protected final String hypothesis;
        private final boolean finalResult;

        ResultEvent(String hypothesis, boolean finalResult) {
            this.hypothesis = hypothesis;
            this.finalResult = finalResult;
        }

        @Override
        protected void execute(RecognitionListener listener) {
            if (finalResult)
                listener.onResult(hypothesis);
            else
                listener.onPartialResult(hypothesis);
        }
    }

    private class OnErrorEvent extends RecognitionEvent {
        private final Exception exception;

        OnErrorEvent(Exception exception) {
            this.exception = exception;
        }

        @Override
        protected void execute(RecognitionListener listener) {
            listener.onError(exception);
        }
    }

    private class TimeoutEvent extends RecognitionEvent {
        @Override
        protected void execute(RecognitionListener listener) {
            listener.onTimeout();
        }
    }

    private boolean stopRecognizerThread() {
        if (processor == null)
            return false;

        dispatcher.removeProcessor(processor);
        processor.processingFinished();
        processor = null;
        return true;
    }


    /**
     * Stops recognition. All listeners should receive final result if there is
     * any. Does nothing if recognition is not active.
     *
     * @return true if recognition was actually stopped
     */
    public boolean stop() {
        boolean result = stopRecognizerThread();
        if (result) {
            //mainHandler.post(new ResultEvent(recognizer.Result(), true));
            ResultEvent r = new ResultEvent(recognizer.Result(), true);
            for (RecognitionListener listener : listeners){
                r.execute(listener);
            }
        }
        return result;
    }

    /**
     * Cancels recognition. Listeners do not receive final result. Does nothing
     * if recognition is not active.
     *
     * @return true if recognition was actually canceled
     */
    public boolean cancel() {
        boolean result = stopRecognizerThread();
        recognizer.Result(); // Reset recognizer state
        return result;
    }

    /**
     * Shutdown the recognizer and release the recorder
     */
    public void shutdown() {
        boolean result = stopRecognizerThread();
        //recorder.release();
    }

    public boolean startListening() {
        if (processor != null)
            return false;

        processor = new RecognizerProcessor();
        dispatcher.addProcessor(processor);
        return true;
    }

    
    private final class RecognizerProcessor implements AudioProcessor {

        private int remainingSamples;
        private int timeoutSamples;
        private final static int NO_TIMEOUT = -1;
        private short[] buffer;
        private byte[] fbuff;
        private ByteBuffer bb;
        private ShortBuffer sb;     //Short view
        private int nread;

        /* It does not make sense to give an AudioProcessor a timeout, so we set the default
            constructor as the only public one. We'll still keep the code for timeouts, just in case
         */

        private RecognizerProcessor(int timeout) {
            buffer = new short[bufferSize];
            fbuff = ByteBuffer.allocate(buffer.length * 2).order(ByteOrder.LITTLE_ENDIAN).array();

            // Vosk requires buffers in short format
            // Endianness SHOULD be correct, but might have to check anyway
            bb = ByteBuffer.wrap(fbuff).order(ByteOrder.BIG_ENDIAN);
            sb = bb.asShortBuffer();
            sb.get(buffer);


            if (timeout != NO_TIMEOUT)
                this.timeoutSamples = timeout * sampleRate / 1000;
            else
                this.timeoutSamples = NO_TIMEOUT;
            this.remainingSamples = this.timeoutSamples;
        }

        public RecognizerProcessor() {
            this(NO_TIMEOUT);
        }

        @Override
        public boolean process(AudioEvent audioEvent) {

            fbuff = converter.toByteArray(audioEvent.getFloatBuffer(), fbuff);
            nread = buffer.length;

            boolean isFinal = recognizer.AcceptWaveform(buffer, nread);

            ResultEvent r;
            if(isFinal){
                r = new ResultEvent(recognizer.Result(), true);
            } else {
                r = new ResultEvent(recognizer.PartialResult(), false);
            }

            for(RecognitionListener listener : listeners){
                r.execute(listener);
            }

            //mainHandler.post(r);

            return false;
        }

        @Override
        public void processingFinished() {
            //mainHandler.removeCallbacksAndMessages(null);
        }
    }

}

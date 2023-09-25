package com.mrousavy.camera.example;

import android.graphics.Matrix;
import android.media.Image;
import android.os.Looper;

import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.mrousavy.camera.frameprocessor.Frame;
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin;
import com.mrousavy.camera.parsers.Orientation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class QRCodeReaderFrameProcessorPlugin extends FrameProcessorPlugin {

    String TAG = "QRCodeAnalyzer";

    BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build();

    BarcodeScanner scanner = BarcodeScanning.getClient(options);

    @Override
    public Object callback(@NotNull Frame frame, @Nullable Map<String, Object> params) {
        Matrix matrix = new Matrix();

        Image image = frame.getImage();
        if (image == null) {
            return "Failed: Image is null";
        }

        int rotationDegrees = Objects.requireNonNull(Orientation.Companion.fromUnionValue(frame.getOrientation())).toDegrees();

        try {
            List<Barcode> barcodes = Tasks.await(scanner.process(image, rotationDegrees, matrix));
            if(barcodes != null && barcodes.size() > 0) {
                Barcode barcode = barcodes.get(0);
                if(barcode != null && barcode.getRawValue() != null) {
                    return barcode.getRawValue();
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
            return "ExecutionException occurred";

        } catch (InterruptedException e) {
            e.printStackTrace();
            return "InterruptedException occurred";
        }
//        try {
//            Log.d(TAG, "Starting to process the image. Moving ahead");
//            scanner.process(image, rotationDegrees, matrix)
//                    .addOnCompleteListener(task -> {
//                        if(task.isSuccessful()) {
//                            Log.d(TAG, "Finished processing image");
//                            List<Barcode> barcodes = task.getResult();
//                            if(barcodes != null && barcodes.size() > 0) {
//                                Barcode barcode = barcodes.get(0);
//                                if(barcode != null && barcode.getRawValue() != null) {
//                                    Log.i(TAG, barcode.getRawValue());
//                                }
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            Log.d(TAG, "Processing failed with exception");
//            e.printStackTrace();
//            return "Task failed: " + e;
//        }

        return "nope";
    }

    QRCodeReaderFrameProcessorPlugin() {

    }
}

//public class QRCodeReaderFrameProcessorPlugin :FrameProcessorPlugin(){
//        override fun callback(frame:Frame,params:MutableMap<String, Any>?):Any{
//        Log.d(TAG,"Analyzing the image")
//        val matrix=Matrix()
//
//        val image:Image?=frame.image
//        if(image==null){
//        Log.e(TAG,"Image is null")
//        return mapOf<String, Any>("task"to"failed: image is null")
//        //imageProxy.close()
//        }
//
//        val rotationDegrees=Orientation.fromUnionValue(frame.orientation)?.toDegrees()
//        ?:Orientation.PORTRAIT.toDegrees()
//
//        var mlKitTask:Task<List<Barcode>>
//
//        try{
//        mlKitTask=qrCodeScanner.process(image,rotationDegrees,matrix)
//        }catch(e:Exception){
//        return mapOf<String, Any>("task"to"failed: exception $e")
//        //imageProxy.close()
////            executor.execute {
////
////                //result.onTaskFailed(e)
////            }
//
//        }
//
//        mlKitTask.addOnCompleteListener{task->
//        if(task.isCanceled()){
//        Log.d("ScannerCancelled",task.result.toString())
////                imageProxy.close()
////                executor.execute {
////                    result.onTaskCancelled()
////                }
//        }else if(task.isSuccessful()){
//        Log.d("ScannerSuccess",task.result.toString())
////                imageProxy.close()
////                executor.execute {
////                    result.onTaskSuccess(task.getResult())
////                }
//        }else{
//        Log.e("ScannerException",task.exception.toString())
////                imageProxy.close()
////                executor.execute {
////                    task.exception?.let { result.onTaskFailed(it) }
////                }
//        }
//        }
//        return mapOf<String, Any>("task"to"ScannerResult: ${mlKitTask.result}")
//        }
//        }
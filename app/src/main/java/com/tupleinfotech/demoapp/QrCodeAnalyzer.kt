package com.tupleinfotech.demoapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QrCodeAnalyzer(
    private val context: Context,
    private val barcodeBoxView: BarcodeBoxView,
    private val previewViewWidth: Float,
    private val previewViewHeight: Float
) : ImageAnalysis.Analyzer {

    private val addedTextViews = mutableListOf<TextView>()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img = image.image
        if (img != null) {
            // Update scale factors
            val scaleX = previewViewWidth / img.height.toFloat()
            val scaleY = previewViewHeight / img.width.toFloat()

            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            // Process image searching for barcodes
            val options = BarcodeScannerOptions.Builder().build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    val barcodeRects = mutableListOf<RectF>()
                    val barcodeValues = mutableListOf<String>()
                    for (barcode in barcodes) {
                        // Add bounding rect to the list
                        barcode.boundingBox?.let { rect ->
                            println(rect.width())
                            println(rect.height())
                            // Adjust bounding rectangle size to match barcode size
                            val adjustedRect = adjustBoundingRect(rect, scaleX, scaleY, rect.width(), rect.height())
                            barcodeRects.add(adjustedRect)
                            barcodeValues.add(barcode.rawValue ?: "")
                        }
                    }

                    // Update BarcodeBoxView with the list of rectangles and barcode values
                    barcodeBoxView.setRects(barcodeRects)
                    // Remove previously added TextViews
                    removeTextViews()
                    barcodeRects.forEachIndexed { index, rect ->
                        addTextViewForBarcode(context, barcodeBoxView, barcodeValues[index], scaleX, scaleY, rect)
                    }
                }
                .addOnFailureListener { }

        }

        image.close()
    }

    private fun addTextViewForBarcode(context: Context, barcodeBoxView: BarcodeBoxView, value: String, scaleX: Float, scaleY: Float, rect: RectF) {
        val textView = TextView(context)
        textView.text = value
        textView.isSingleLine = true
        textView.textSize = 12f
        textView.setTextColor(Color.WHITE)

        val layoutParams = FrameLayout.LayoutParams(rect.width().toInt(), rect.height().toInt())
        layoutParams.leftMargin = rect.left.toInt()
        layoutParams.rightMargin = rect.right.toInt()
        layoutParams.topMargin = (rect.bottom + 16).toInt()
        textView.layoutParams = layoutParams

        val parentViewGroup = barcodeBoxView.parent as? ViewGroup
        parentViewGroup?.addView(textView)
        addedTextViews.add(textView)
    }

    private fun removeTextViews() {
        addedTextViews.forEach { textView ->
            val parentViewGroup = textView.parent as? ViewGroup
            parentViewGroup?.removeView(textView)
        }
        addedTextViews.clear()
    }

    private fun adjustBoundingRect(rect: Rect, scaleX: Float, scaleY: Float, barcodeWidth: Int, barcodeHeight: Int): RectF {
        val barcodeSize = maxOf(barcodeWidth, barcodeHeight)

        Toast.makeText(context, "Barcode size: $barcodeSize", Toast.LENGTH_SHORT).show()
//        println("Barcode size: $barcodeSize")
//        println("Barcode size: ${rect.exactCenterX()}")
//        println("Barcode size: ${rect.exactCenterY()}")
        // Adjust bounding rectangle size to match barcode size
        val left    = rect.left.toFloat() * scaleX
        val top     = rect.top.toFloat() * scaleY
        val right   = left + barcodeWidth * scaleX
        val bottom  = top  + barcodeHeight * scaleY
        return RectF(left, top, right, bottom)
    }
}


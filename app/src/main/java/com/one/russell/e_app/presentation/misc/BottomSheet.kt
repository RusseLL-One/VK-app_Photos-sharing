package com.one.russell.e_app.presentation.misc

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.one.russell.e_app.R
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.io.File


class BottomSheet(private val photo: File?, private val sendListener: OnSendClickedListener) : BottomSheetDialogFragment() {

    interface OnSendClickedListener {
        fun onSendClicked(photo: File, message: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { bottomSheet ->
            Handler().postDelayed({
                val d = bottomSheet as BottomSheetDialog
                val bottomSheetBehavior = d.behavior
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                txt_message.requestFocus()
            }, 0)
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_close.setOnClickListener { dismissAllowingStateLoss()  }

        val cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resources.getDimension(R.dimen.corner_raduis),
            resources.displayMetrics
        ).toInt()

        Glide
            .with(requireContext())
            .load(photo)
            .transform(RoundedCorners(cornerRadius))
            .into(img_preview)

        if (photo != null) {
            btn_send.setOnClickListener {
                sendListener.onSendClicked(photo, txt_message.text.toString())
                dismissAllowingStateLoss()
            }
        }
    }
}
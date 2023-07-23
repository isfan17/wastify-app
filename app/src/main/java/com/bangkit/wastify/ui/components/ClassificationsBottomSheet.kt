package com.bangkit.wastify.ui.components

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.utils.Helper.doubleToPercentageString
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ClassificationsBottomSheet(
    private val classifications: List<Classification>
): BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.classifications_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classifications.forEach {
            view.findViewById<ChipGroup>(R.id.cg_classifications)
                .addView(createClassificationChip(requireContext(), it))
        }
    }

    private fun createClassificationChip(
        context: Context,
        classification: Classification
    ): Chip {
        return Chip(context).apply {
            text = "${classification.name} : ${doubleToPercentageString(classification.percentage)}"
        }
    }

    companion object {
        const val TAG = "ClassificationsBottomSheet"
    }
}
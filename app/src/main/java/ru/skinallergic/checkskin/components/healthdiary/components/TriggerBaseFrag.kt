package ru.skinallergic.checkskin.components.healthdiary.components

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.entrance.EntranceActivity

const val TRIGGERS_BUNDLE="triggers"
abstract class TriggerBaseFrag : Fragment() {
    lateinit var allButtons : List<AppCompatButton>

    var checkMark: Drawable? = null
    var checkMark_off: Drawable? = null
    var icon0: Drawable? = null
    var icon1: Drawable? = null
    var icon2: Drawable? = null
    var icon3: Drawable? = null
    var icon4: Drawable? = null
    var icon5: Drawable? = null
    var icon6: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkMark_off = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_cheakbox_blue_off)
        checkMark_off!!.setBounds(0, 0, 45, 45)
        initIcons()

    }

    fun initIcons() {
        checkMark = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_cheakbox_blue_on)
        checkMark!!.setBounds(0, 0, 45, 45)
        icon0 = ContextCompat.getDrawable(requireActivity(), R.drawable.my_icon_trigger_0)
        icon0!!.setBounds(0, 0, 75, 75)
        icon1 = ContextCompat.getDrawable(requireActivity(), R.drawable.my_icon_trigger_1)
        icon1!!.setBounds(0, 0, 75, 75)
        icon2 = ContextCompat.getDrawable(requireActivity(), R.drawable.my_icon_trigger_2)
        icon2!!.setBounds(0, 0, 75, 75)
        icon3 = ContextCompat.getDrawable(requireActivity(), R.drawable.my_icon_trigger_3)
        icon3!!.setBounds(0, 0, 75, 75)
        icon4 = ContextCompat.getDrawable(requireActivity(), R.drawable.my_icon_trigger_4)
        icon4!!.setBounds(0, 0, 75, 75)
        icon5 = ContextCompat.getDrawable(requireActivity(), R.drawable.my_icon_trigger_5)
        icon5!!.setBounds(0, 0, 75, 75)
        icon6 = ContextCompat.getDrawable(requireActivity(), R.drawable.my_icon_trigger_5)
        icon6!!.setBounds(0, 0, 75, 75)
    }

    fun initButtons(view: View) {
        val trig0: AppCompatButton = view.findViewById(R.id.trig_0)
        val trig1: AppCompatButton = view.findViewById(R.id.trig_1)
        val trig2: AppCompatButton = view.findViewById(R.id.trig_2)
        val trig3: AppCompatButton = view.findViewById(R.id.trig_3)
        val trig4: AppCompatButton = view.findViewById(R.id.trig_4)
        val trig5: AppCompatButton = view.findViewById(R.id.trig_5)
        val trig6: AppCompatButton = view.findViewById(R.id.trig_6)
        defaultButton(trig0, icon0!!)
        defaultButton(trig1, icon1!!)
        defaultButton(trig2, icon2!!)
        defaultButton(trig3, icon3!!)
        defaultButton(trig4, icon4!!)
        defaultButton(trig5, icon5!!)
        defaultButton(trig6, icon6!!)

        allButtons= arrayListOf(trig0, trig1, trig2, trig3, trig4, trig5, trig6)
        for (button in allButtons){
            changeVisibility(button, false)
        }
    }


    fun defaultButton(button: AppCompatButton, icon: Drawable) {
        button.setCompoundDrawables(icon, null, null, null)
    }

    open fun changeVisibility(button: AppCompatButton, visible: Boolean) {
        val drawables = button.compoundDrawables
        val icon = drawables[0]
        val checkMark_ = checkMark!!
        if (visible) {
            button.setCompoundDrawables(icon, null, checkMark_, null)
        } else button.setCompoundDrawables(icon, null, null, null)
    }

    fun toEntrance() {
        val intent = Intent(requireActivity(), EntranceActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
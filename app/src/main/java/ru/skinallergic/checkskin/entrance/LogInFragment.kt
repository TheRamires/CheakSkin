package ru.skinallergic.checkskin.entrance

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentLogInBinding
import ru.skinallergic.checkskin.di.MyViewModelFactory
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorNumber
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorNumber.helpExtractNumber
import ru.skinallergic.checkskin.entrance.pojo.VKUser
import ru.skinallergic.checkskin.utils.POLITICS
import ru.skinallergic.checkskin.view_models.AccountViewModel
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl
import java.util.*
import kotlin.collections.ArrayList


class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private lateinit var accountViewModel: AccountViewModelImpl
    private var callbackManager: CallbackManager? = null
    private var mDialog: ProgressDialog? = null
    private lateinit var loginButton:LoginButton
    private var accessToken :String?=null
    private var network: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory: MyViewModelFactory = App.instance.appComponent.viewModelFactory
        accountViewModel=ViewModelProvider(requireActivity(), viewModelFactory).get(AccountViewModelImpl::class.java)
        subscribeCheakNumber(accountViewModel)

        LoginManager.getInstance().logOut()//костыль
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding= FragmentLogInBinding.inflate(inflater).apply {
            editText.setSelection(2)
/*
            Loger.log("editText.getText().toString() "+editText.getText().toString())
            ValidatorNumber.cheackNumber(editText.getText().toString())*/

            FragmentLogInBinding.inflate(inflater)
            fragment=this@LogInFragment

            val numberLast=accountViewModel.currentUser.value?.tel?.let {
                if(it != "") {
                    editText.setText(it)
                }
            }
            logInIsTrue=ValidatorNumber.validate()

            editText.doAfterTextChanged {
                ValidatorNumber.cheackNumber(it!!)
                if (ValidatorNumber.isEntered) {
                    val number = "+"+helpExtractNumber(it)
                    val userTemp=accountViewModel.getCurrentUserNonNull()
                    userTemp.tel=number
                    accountViewModel.currentUser.value=userTemp
                    println("userTemp "+userTemp)
                }
                logInIsTrue=ValidatorNumber.validate()
            }

            cheakBox1.setOnClickListener {
                ValidatorNumber.cheackBox(cheakBox1, cheakBox2)
                logInIsTrue=ValidatorNumber.validate()
            }

            cheakBox2.setOnClickListener {
                ValidatorNumber.cheackBox(cheakBox1, cheakBox2)
                logInIsTrue=ValidatorNumber.validate()
            }
        }

        callbackManager = CallbackManager.Factory.create()
        loginButton = binding.loginButton

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                loginResult?.let { onSuccesses(it) }
                // App code
                val request = GraphRequest.newMeRequest(
                        loginResult!!.accessToken
                ) { `object`, response ->
                    Loger.log("LoginActivity " + response.toString())

                    // Application code
                    val name = `object`.getString("name")
                    Loger.log("LoginActivity name")
                    val userTemp=accountViewModel.currentUser.value
                    userTemp?.name=name
                    accountViewModel.currentUser.value=userTemp
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                Loger.log("facebook cancel")
            }

            override fun onError(error: FacebookException) {}
        })
        if (AccessToken.getCurrentAccessToken() != null) {
            Loger.log("faceboock onSuccess")

            //Just set User ID
            val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { `object`, response ->Unit }

            //Request Graph API
            val parameters = Bundle()
            parameters.putString("fields", "id, email, location, birthday, friends")
            request.parameters = parameters
            request.executeAsync()
        }

        return binding.root
    }

    fun logIn(view: View){
/*
    //Example----------------------------------------------------------------------------------
        val number=accountViewModel.currentUser.number
        registrationViewModel.sendingCode(number)
        Navigation.findNavController(binding.root).navigate(R.id.sms1Fragment)
        //------------------------------------------------------------------------------------

        */
        val number=accountViewModel.currentUser.value?.tel
        Loger.log("ON Click" + number)
        accountViewModel.checkNumber(number)
    }

    private fun subscribeCheakNumber(viewModel: AccountViewModel){
        viewModel.numberIsCheacked.observe(this, Observer {
            if (it) {
                viewModel.numberIsCheacked.value = false
                val number = accountViewModel.currentUser.value?.tel
                viewModel.sendingCode(number!!)
                Loger.log("Log In " + accountViewModel.currentUser.value?.tel)
                Navigation.findNavController(binding.root).navigate(R.id.action_logInFragment_to_sms1Fragment)
            } else {
                Loger.log("Такого номера не существует")
                Toast.makeText(requireContext(), "Такого номера не существует", Toast.LENGTH_SHORT)
            }
        })
    }

    fun logInVk(view: View){
        VK.login(requireActivity(), arrayListOf(VKScope.OFFLINE))
    }

    fun logInFacebook(view: View){
        loginButton.performClick()
    }

    fun onSuccesses(loginResult: LoginResult) {
        Loger.log("faceboock onSuccess")
        mDialog = ProgressDialog(requireContext())
        mDialog!!.setMessage("Retrieving data...")
        mDialog!!.show()

        val accessToken = loginResult.accessToken.token
        Loger.log("facebook accessToken " + accessToken)
        val network="facebook"
        accountViewModel.netWorkLogIn(network, accessToken)

        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->

            mDialog!!.dismiss()

            Loger.log("onCompleted: $`object`")
        }

        //Request Graph API
        val parameters = Bundle()
        parameters.putString("fields", "id, email, location, birthday, friends")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val facebook: Boolean=callbackManager!!.onActivityResult(requestCode, resultCode, data)
        if(facebook){
            return
        }

        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                accountViewModel.getVkUser()
                // binding.textView8.setText(token.toString())
                Loger.log("VKAccessToken " + token.accessToken)
                val accessToken=token.accessToken
                val network="vk"
                accountViewModel.netWorkLogIn(network, accessToken)

                // User passed authorization
            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(requireContext(), "onLoginFailed " + errorCode, Toast.LENGTH_LONG).show()
                //binding.textView8.setText(errorCode.toString())
                // User didn't pass authorization
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            Toast.makeText(requireContext(), "vkkk " + requestCode, Toast.LENGTH_LONG).show()
        }
    }
    fun toPolitic(v: View){
        val address = Uri.parse(POLITICS)
        val openLinkIntent: Intent = Intent(Intent.ACTION_VIEW, address)

        if (openLinkIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(openLinkIntent)
        } else {
                Loger.log("Intent"+ "Не получается обработать намерение!")
        }

    }
}

class VKUsersRequest: VKRequest<List<VKUser>> {
    constructor(uids: IntArray = intArrayOf()): super("users.get") {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
        addParam("fields", "photo_200")
    }

    override fun parse(r: JSONObject): List<VKUser> {
        val users = r.getJSONArray("response")
        val result = ArrayList<VKUser>()
        for (i in 0 until users.length()) {
            result.add(VKUser.parse(users.getJSONObject(i)))
        }
        return result
    }
}
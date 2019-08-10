package com.example.futsalgoadmin.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.util.Patterns;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.futsalgoadmin.Konfigurasi;
import com.example.futsalgoadmin.R;
import com.example.futsalgoadmin.data.LoginRepository;

import org.json.JSONException;
import org.json.JSONObject;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        AndroidNetworking.post(Konfigurasi.ADMIN)
                .addBodyParameter("method", "login")
                .addBodyParameter("email", username)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.opt("status") == TRUE) {
                            try {
                                JSONObject data = response.getJSONObject("data");
                                loginResult.setValue(
                                        new LoginResult(
                                                TRUE,
                                                "",
                                                data.optInt("id"),
                                                data.optString("telp"),
                                                data.optString("email"),
                                                data.optString("bank"),
                                                data.optString("nama_rekening"),
                                                data.optString("no_rekening"),
                                                data.optString("jam_buka"),
                                                data.optString("jam_tutup")
                                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loginResult.setValue(new LoginResult(FALSE, response.opt("msg").toString()));
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "isi error " + error.getErrorBody());
                        loginResult.setValue(new LoginResult(FALSE,"Login failed"));
                    }
                });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 5;
    }
}

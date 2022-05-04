package bobrchess.of.by.belaruschess.presenter

import android.view.View

import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.view.activity.PasswordResetContractView

/**
 * Created by Misha on 05.05.2022.
 */

interface PasswordResetPresenter : BasePresenter {
    fun authorization(userDTO: UserDTO)
    fun attachView(activity: PasswordResetContractView)
    fun attachViewComponent(view: View)
}

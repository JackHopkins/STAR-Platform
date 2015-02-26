package controllers;

import play.api.mvc.{RequestHeader, Request}
import play.api.templates.Html
import play.api.templates.Txt
import securesocial.core.SecuredRequest
import play.api.data.Form
import securesocial.controllers.Registration.RegistrationInfo
import securesocial.controllers.PasswordChange.ChangeInfo
import securesocial.controllers.TemplatesPlugin

class SecureSocialTemplatesPlugin(application: play.Application) extends TemplatesPlugin 
{
 /**
   * Returns the html for the login page
   * @param request
   * @tparam A
   * @return
   */
  override def getLoginPage[A](implicit request: Request[A], form: Form[(String, String)], msg: Option[String] = None): Html = {
    views.html.ss.login(request, form, msg)
  }

  override def getNotAuthorizedPage[A](implicit request: play.api.mvc.Request[A]): play.api.templates.Html = {
    views.html.ss.notAuthorized(request)
  }
  /**
   * Returns the html for the signup page
   *
   * @param request
   * @tparam A
   * @return
   */
  override def getSignUpPage[A](implicit request: Request[A], form: Form[RegistrationInfo], token: String): Html = {
    views.html.ss.Registration.signUp(request, form, token)
  }

  /**
   * Returns the html for the start signup page
   *
   * @param request
   * @tparam A
   * @return
   */
  override def getStartSignUpPage[A](implicit request: Request[A], form: Form[String]): Html = {
    views.html.ss.Registration.startSignUp(request, form)
  }

  /**
   * Returns the html for the reset password page
   *
   * @param request
   * @tparam A
   * @return
   */
  override def getStartResetPasswordPage[A](implicit request: Request[A], form: Form[String]): Html = {
    views.html.ss.Registration.startResetPassword(request, form)
  }

  /**
   * Returns the html for the start reset page
   *
   * @param request
   * @tparam A
   * @return
   */
  override def getResetPasswordPage[A](implicit request: Request[A], form: Form[(String, String)], token: String): Html = {
    views.html.ss.Registration.resetPasswordPage(request, form, token)
  }

   /**
   * Returns the html for the change password page
   *
   * @param request
   * @param form
   * @tparam A
   * @return
   */
  override def getPasswordChangePage[A](implicit request: SecuredRequest[A], form: Form[ChangeInfo]): Html = {
    views.html.ss.passwordChange(request, form)      
  }


  /**
   * Returns the email sent when a user starts the sign up process
   *
   * @param token the token used to identify the request
   * @param request the current http request
   * @return a String with the html code for the email
   */
  override def getSignUpEmail(token: String)(implicit request: RequestHeader): (Option[Txt], Option[Html]) = {
    (None, Some(views.html.ss.mails.signUpEmail(request, token)))
  }

  /**
   * Returns the email sent when the user is already registered
   *
   * @param user the user
   * @param request the current request
   * @return a String with the html code for the email
   */
  override def getAlreadyRegisteredEmail(user: securesocial.core.Identity)(implicit request: play.api.mvc.RequestHeader): (Option[play.api.templates.Txt], Option[play.api.templates.Html]) = {
    (None, Some(views.html.ss.mails.alreadyRegisteredEmail(request, user)))
  }

  /**
   * Returns the welcome email sent when the user finished the sign up process
   *
   * @param user the user
   * @param request the current request
   * @return a String with the html code for the email
   */
  override def getWelcomeEmail(user: securesocial.core.Identity)(implicit request: play.api.mvc.RequestHeader): (Option[play.api.templates.Txt], Option[play.api.templates.Html]) = {
    (None, Some(views.html.ss.mails.welcomeEmail(request, user)))
  }

  /**
   * Returns the email sent when a user tries to reset the password but there is no account for
   * that email address in the system
   *
   * @param request the current request
   * @return a String with the html code for the email
   */
  override def getUnknownEmailNotice()(implicit request: RequestHeader): (Option[play.api.templates.Txt], Option[play.api.templates.Html]) = {
    (None, Some(views.html.ss.mails.unknownEmailNotice(request)))
  }

  /**
   * Returns the email sent to the user to reset the password
   *
   * @param user the user
   * @param token the token used to identify the request
   * @param request the current http request
   * @return a String with the html code for the email
   */
  override def getSendPasswordResetEmail(user: securesocial.core.Identity,token: String)(implicit request: play.api.mvc.RequestHeader): (Option[play.api.templates.Txt], Option[play.api.templates.Html]) = {
    (None, Some(views.html.ss.mails.passwordResetEmail(request, user, token)))
  }

  /**
   * Returns the email sent as a confirmation of a password change
   *
   * @param user the user
   * @param request the current http request
   * @return a String with the html code for the email
   */
  override def getPasswordChangedNoticeEmail(user: securesocial.core.Identity)(implicit request: play.api.mvc.RequestHeader): (Option[play.api.templates.Txt], Option[play.api.templates.Html]) = {
    (None, Some(views.html.ss.mails.passwordChangedNotice(request, user)))
  }

}

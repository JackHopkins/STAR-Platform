package controllers;

import play.api.mvc.{RequestHeader, Request}
//import play.api.templates.Html
import play.api.templates.Txt
import play.twirl.api.Html

import securesocial.core.{BasicProfile, RuntimeEnvironment}
import play.api.data.Form
import play.api.i18n.Lang
import securesocial.controllers.ViewTemplates

import securesocial.controllers.RegistrationInfo
import securesocial.controllers.ChangeInfo

import views.html.ss.login
import views.html.ss.Registration.signUp
import views.html.ss.Registration.startSignUp
import views.html.ss.Registration.startResetPassword
import views.html.ss.passwordChange
import views.html.ss.notAuthorized

class ViewTemplatesPlugin(env: RuntimeEnvironment[_]) extends ViewTemplates.Default(env) {  
  
	override def getLoginPage(form: Form[(String, String)], msg: Option[String] = None)(implicit request: RequestHeader, lang: Lang): Html = {
		views.html.ss.login(form, msg)(request, env)
	}
  
  	override def getSignUpPage(form: Form[RegistrationInfo], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
      views.html.ss.Registration.signUp(form, token)(request, env)
  	}

    override def getStartSignUpPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
      views.html.ss.Registration.startSignUp(form)(request, env)
    }

    override def getStartResetPasswordPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
      views.html.ss.Registration.startResetPassword(form)(request, env)
    }

    override def getResetPasswordPage(form: Form[(String, String)], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
      views.html.ss.Registration.resetPasswordPage(form, token)(request, env)
    }

    override def getPasswordChangePage(form: Form[ChangeInfo])(implicit request: RequestHeader, lang: Lang): Html = {
      views.html.ss.passwordChange(form)(request, env)
    }

    override def getNotAuthorizedPage(implicit request: RequestHeader, lang: Lang): Html = {
      views.html.ss.notAuthorized()(request, env)
    }
}

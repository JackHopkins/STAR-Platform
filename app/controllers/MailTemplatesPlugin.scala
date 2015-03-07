package controllers;

import play.api.mvc.{RequestHeader, Request}
import play.twirl.api.Txt
import play.twirl.api.Html

import securesocial.core.{BasicProfile, RuntimeEnvironment}
import play.api.data.Form
import play.api.i18n.Lang
import securesocial.controllers.MailTemplates

import securesocial.controllers.RegistrationInfo
import securesocial.controllers.ChangeInfo

import views.html.ss.mails.signUpEmail
import views.html.ss.mails.welcomeEmail
import views.html.ss.mails.alreadyRegisteredEmail
import views.html.ss.mails.unknownEmailNotice
import views.html.ss.mails.passwordChangedNotice
import views.html.ss.mails.alreadyRegisteredEmail
import views.html.ss.mails.passwordChangedNotice

class MailTemplatesPlugin(env: RuntimeEnvironment[_]) extends MailTemplates.Default(env) {  
  
	override implicit val implicitEnv = env

    override def getSignUpEmail(token: String)(implicit request: RequestHeader, lang: Lang): (Option[Txt], Option[Html]) = {
      (None, Some(views.html.ss.mails.signUpEmail(token)(request)))
    }

    override def getAlreadyRegisteredEmail(user: BasicProfile)(implicit request: RequestHeader, lang: Lang): (Option[Txt], Option[Html]) = {
      (None, Some(views.html.ss.mails.alreadyRegisteredEmail(user)(request, env)))
    }

    override def getWelcomeEmail(user: BasicProfile)(implicit request: RequestHeader, lang: Lang): (Option[Txt], Option[Html]) = {
      (None, Some(views.html.ss.mails.welcomeEmail(user)(request, env)))
    }

    override def getUnknownEmailNotice()(implicit request: RequestHeader, lang: Lang): (Option[Txt], Option[Html]) = {
      (None, Some(views.html.ss.mails.unknownEmailNotice()(request)))
    }

    override def getSendPasswordResetEmail(user: BasicProfile, token: String)(implicit request: RequestHeader, lang: Lang): (Option[Txt], Option[Html]) = {
      (None, Some(views.html.ss.mails.passwordResetEmail(user, token)(request, env)))
    }

    override def getPasswordChangedNoticeEmail(user: BasicProfile)(implicit request: RequestHeader, lang: Lang): (Option[Txt], Option[Html]) = {
      (None, Some(views.html.ss.mails.passwordChangedNotice(user)(request, env)))
    }
}

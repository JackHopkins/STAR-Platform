package service

import securesocial.core.RuntimeEnvironment
import scala.collection.immutable.ListMap

import securesocial.controllers.{ MailTemplates, ViewTemplates }
import securesocial.core.authenticator._
import securesocial.core.providers._
import securesocial.core.providers.utils.{ Mailer, PasswordHasher, PasswordValidator }
import securesocial.core.services._

import controllers.ViewTemplatesPlugin
import controllers.MailTemplatesPlugin

class MyEnvironment extends RuntimeEnvironment.Default[UserCredential] {
  override val userService: securesocial.core.services.UserService[UserCredential] = new UserService()
  override lazy val viewTemplates: ViewTemplates = new ViewTemplatesPlugin(this)  
  override lazy val mailTemplates: MailTemplates = new MailTemplatesPlugin(this)  
  
  override lazy val providers = ListMap(
      include(new UsernamePasswordProvider[UserCredential](userService, avatarService, viewTemplates, passwordHashers))
    )
  
}
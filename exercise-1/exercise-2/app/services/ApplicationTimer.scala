package services

import java.time.{Clock, Instant}
import javax.inject._
import play.api.Logger
import play.api.inject.ApplicationLifecycle
import scala.concurrent.Future

/**
 * This class demonstrates how to run code when the
 * application starts and stops. It starts a timer when the
 * application starts. When the application stops it prints out how
 * long the application was running for.
 *
 * This class is registered for Guice dependency injection in the
 * [[Module]] class. We want the class to start when the application
 * starts, so it is registered as an "eager singleton". See the code
 * in the [[Module]] class to see how this happens.
 *
 * This class needs to run code when the server stops. It uses the
 * application's [[ApplicationLifecycle]] to register a stop hook.
 */
@Singleton
class ApplicationTimer @Inject() (clock: Clock, appLifecycle: ApplicationLifecycle) {

  private val start: Instant = clock.instant

  appLifecycle.addStopHook { () =>
    val stop: Instant = clock.instant
    Future.successful(())
  }
}

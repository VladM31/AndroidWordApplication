package can.lucky.of.addword.di

import can.lucky.of.addword.ui.listeners.spotlights.AddWordByTextFragmentListener
import can.lucky.of.addword.ui.listeners.spotlights.RecognizeWordTasksFragmentListener
import can.lucky.of.core.ui.listeners.spotlights.SpotlightsFragmentListener
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val listenerModule = module {

    single<SpotlightsFragmentListener>(named("AddWordByTextFragmentListener")) {
        AddWordByTextFragmentListener(
            spotlightManager = get()
        )
    }

    single<SpotlightsFragmentListener>(named("RecognizeWordTasksFragmentListener")) {
        RecognizeWordTasksFragmentListener(
            spotlightManager = get()
        )
    }

}
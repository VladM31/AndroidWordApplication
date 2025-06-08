package can.lucky.of.addword.di

import can.lucky.of.addword.domain.vms.AddWordByImageVm
import can.lucky.of.addword.domain.vms.AddWordByQrCodeVm
import can.lucky.of.addword.domain.vms.AddWordByTextVm
import can.lucky.of.addword.domain.vms.ChooseAddWordVm
import can.lucky.of.addword.domain.vms.DefaultAddWordVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


internal val viewModelDi = module {
    viewModel{
        AddWordByImageVm(
            wordRecognizeManager = get()
        )
    }

    viewModel {
        DefaultAddWordVm(
            wordManager = get(),
            subscribeCacheManager = get()
        )
    }

    viewModel{
        ChooseAddWordVm(
            subscribeCacheManager = get()
        )
    }

    viewModel {
        AddWordByQrCodeVm(
            shareUserWordManager = get()
        )
    }

    viewModel{
        AddWordByTextVm(
            wordRecognizeManager = get()
        )
    }
}
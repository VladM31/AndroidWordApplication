package com.generagames.happy.town.farm.wordandroid.di.configs

import com.generagames.happy.town.farm.wordandroid.domain.vms.CreatePlayListViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.EditPlayListVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.PinUserWordsViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListChooserDialogVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListDetailsVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListFilterVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.ScanPlayListVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.SharePlayListVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.ShareUserWordVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.UserWordFilterVm
import com.generagames.happy.town.farm.wordandroid.domain.vms.UserWordsViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.WordFilterViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.WordViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.WordsViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.infos.InstructionViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.CardPayViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.ChoosePayViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.SubCostViewModel
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.SubscribeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        WordsViewModel(
            wordManager = get(),
            userCacheManager = get()
        )
    }

    viewModel{
        WordFilterViewModel()
    }
    viewModel {
        PinUserWordsViewModel(
            wordManager = get(),
            userWordManager = get(),
            subscribeCacheManager = get()
        )
    }
    viewModel {
        WordViewModel(
            wordManager = get()
        )
    }

    viewModel {
        UserWordsViewModel(
            userWordManager = get(),
            pinPlayListManager = get()
        )
    }

    viewModel{
        PlayListViewModel(
            playListManager = get()
        )
    }

    viewModel {
        PlayListChooserDialogVm(
            playListManager = get()
        )
    }

    viewModel {
        PlayListDetailsVm(
            playListManager = get(),
            pinPlayListManager = get(),
            exerciseWordManager = get(),
            exerciseTransactionManager = get()
        )
    }

    viewModel {
        CreatePlayListViewModel(
            playListManager = get()
        )
    }

    viewModel {
        SubscribeViewModel(
            subscribeCacheManager = get()
        )
    }

    viewModel {
        CardPayViewModel(
            userCacheManager = get(),
            payManager = get(),
            payPropositionManager = get(),
            subscribeCacheManager = get()
        )
    }

    viewModel {
        SubCostViewModel(
            payManager = get(),
            payPropositionManager = get()
        )
    }

    viewModel {
        EditPlayListVm(
            playListManager = get()
        )
    }


    viewModel {
        PlayListFilterVm()
    }

    viewModel {
        UserWordFilterVm()
    }

    viewModel {
        ShareUserWordVm(
            shareUserWordManager = get()
        )
    }

    viewModel {
        SharePlayListVm(
            sharePlayListManager = get()
        )
    }

    viewModel {
        ScanPlayListVm(
            sharePlayListManager = get()
        )
    }

    viewModel {
        ChoosePayViewModel(
            payManager = get(),
            payPropositionManager = get(),
            subscribeCacheManager = get()
        )
    }

    viewModel {
        InstructionViewModel(
            instructionManager = get()
        )
    }
}
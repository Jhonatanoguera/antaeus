package io.pleo.antaeus.core.services

import java.time.LocalDate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TaskSchedulerService(
        private val billingService: BillingService
) {
    /**
     *  Runs an scheduledServiceExecutor
      */
    fun automatedPaymentOfPendingInvoices() {
        val schedulerService = Executors.newScheduledThreadPool(1)
        schedulerService.scheduleAtFixedRate(
                automatedInvoicePayment,0,1, TimeUnit.DAYS)
    }

    /**
     *  Quick runnable to call pendingInvoicesPayment. Verifies for the day to be the 1st before
     *  executing the function
     */
    val automatedInvoicePayment = Runnable {
        if(LocalDate.now().dayOfMonth == 1) billingService.pendingInvoicesPayment()
    }
}
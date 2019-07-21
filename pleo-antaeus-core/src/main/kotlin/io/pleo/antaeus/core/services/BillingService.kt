package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging
import kotlin.math.log

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService
) {

    private val logger = KotlinLogging.logger {}

    /** Single invoice payment function
     *
     * @param Invoice to pay
     * @return Invoice object whose InvoiceStatus depends on charge function's return
     */

    fun singleInvoicePayment(invoice: Invoice): Invoice {
       var isValid: Boolean
       try {
           isValid = paymentProvider.charge(invoice)
       }
       catch (ex: Exception) {
           when(ex) {
               is CustomerNotFoundException -> {
                   logger.error { "Customer ${invoice.customerId} was not found" }
                   isValid = false
               }
               is CurrencyMismatchException -> {
                  logger.error { "There's an issue with the custome ${invoice.customerId} " +
                          "currency" }
                  isValid = false
               }
               is NetworkException -> {
                   logger.error { "A network error has interrupted the process. Please manually " +
                           "restart the task or process the invoice individually "}
                   isValid = false
               }
               else -> {
                   logger.error { "Unknown error while executing singleInvoicePayment" }
                   isValid = false }
           }
       }
       return if (isValid) invoice.copy(status = InvoiceStatus.PAID) else invoice
   }

    /** Function to pay an invoice based on Id
     *
     * @param Id of the invoice to pay
     * @return Invoice after being processed
     */
    fun singleInvoicePayment(id: Int): Invoice {
        var invoiceToProcess = invoiceService.fetch(id)
        return singleInvoicePayment(invoiceToProcess)
    }

    /** Function to pay any invoice flagged as Pending
     *
     * @return List of paid invoices
     */
    fun pendingInvoicesPayment() : List<Invoice> {
        return invoiceService.fetchPendingInvoices().map { singleInvoicePayment((it)) }
    }

    /** Function to pay a list of invoices. Status not considered
     *
     * @param List of Invoices to pay
     * @return List of paid invoices
     */
    fun bulkInvoicesPayment(invoicesList: List<Invoice>): List<Invoice> {
        return invoicesList.map { singleInvoicePayment(it) }
    }
}
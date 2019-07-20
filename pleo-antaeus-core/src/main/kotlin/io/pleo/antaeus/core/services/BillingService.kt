package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService
) {

    // private val logger = KotlinLogging.logger {}

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
       catch (e: Exception) {
           isValid = false
           // Handling exceptions
       }
       return if (isValid) invoice.copy(status = InvoiceStatus.PAID) else invoice
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
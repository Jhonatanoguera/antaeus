package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Test

class BillingServiceTest {
    // Testing objects
    private val sampleMoney = Money(50.toBigDecimal(), Currency.USD)
    private val testInvoice = Invoice(5, 10, sampleMoney, InvoiceStatus.PENDING)
    private val testCustomerFailureInvoice = Invoice(5, 11, sampleMoney, InvoiceStatus.PENDING)
    private val testCurrencyFailureInvoice = testCustomerFailureInvoice.copy(id = 6)
    private val testNetworkFailureInvoice = testCustomerFailureInvoice.copy(id = 7)
    // Mocks
    private val paymentProvider = mockk<PaymentProvider> {
        every { charge(testInvoice) } returns true
        every { charge(testCustomerFailureInvoice) } throws CustomerNotFoundException(11)
        every { charge(testCurrencyFailureInvoice) } throws CurrencyMismatchException(11, 6)
        every { charge(testNetworkFailureInvoice) } throws NetworkException()
    }
    private val invoiceService = mockk<InvoiceService> {

    }
    // Service to test
    private val billingService = BillingService(paymentProvider = paymentProvider,
            invoiceService = invoiceService)

    @Test
    fun `will pay a correctly formed invoice`() {
        assert (billingService.singleInvoicePayment(testInvoice) ==
                testInvoice.copy(status = InvoiceStatus.PAID))
    }

    @Test
    fun `will not pay the invoice due to a CustomerException`() {
        assert (billingService.singleInvoicePayment(testCustomerFailureInvoice) == testCustomerFailureInvoice)
        verify(exactly = 1) {
            paymentProvider.charge(testCustomerFailureInvoice)
        }
    }
    @Test
    fun `will not pay the invoice due to a CurrencyException`() {
        assert (billingService.singleInvoicePayment(testCurrencyFailureInvoice) == testCurrencyFailureInvoice)
        verify(exactly = 1) {
            paymentProvider.charge(testCurrencyFailureInvoice)
        }
    }

    @Test
    fun `will not pay the invoice due to a NetworkException`() {
        assert (billingService.singleInvoicePayment(testNetworkFailureInvoice) == testNetworkFailureInvoice)
        verify(exactly = 1) {
            paymentProvider.charge(testNetworkFailureInvoice)
        }
    }
}
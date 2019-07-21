/*
    Implements the data access layer (DAL).
    This file implements the database queries used to fetch and insert rows in our database tables.

    See the `mappings` module for the conversions between database rows and Kotlin objects.
 */

package io.pleo.antaeus.data

import io.pleo.antaeus.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.and
import kotlin.random.Random

class AntaeusDal(private val db: Database) {
    fun fetchInvoice(id: Int): Invoice? {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            // Returns the first invoice with matching id.
            InvoiceTable
                .select { InvoiceTable.id.eq(id) }
                .firstOrNull()
                ?.toInvoice()
        }
    }

    fun fetchInvoices(): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .selectAll()
                .map { it.toInvoice() }
        }
    }

    /** Simple override to create a db query based on an InvoiceStatus
     *
     * @param status one of the existant status for an invoice
     * @return List<Invoice> with the filtered invoices by status
     */
    fun fetchInvoices(status: InvoiceStatus): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .select { InvoiceTable.status.eq(status.name) }
                .map { it.toInvoice() }
        }
    }

    /** Simple override to create a db query based on an InvoiceStatus and MembershipType
     *
     * @param status one of the existant status for an invoice
     * @return List<Invoice> with the filtered invoices by status
     */
    fun fetchInvoices(status: InvoiceStatus, membership: MembershipType): List<Invoice> {
        return transaction(db) {
            (InvoiceTable innerJoin CustomerTable)
                    .select { InvoiceTable.customerId.eq(CustomerTable.id) and CustomerTable
                            .membership.eq(membership.name) and InvoiceTable.status.eq(status.name)}
                    .map { it.toInvoice() }
        }
    }

    fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice? {
        val id = transaction(db) {
            // Insert the invoice and returns its new id.
            InvoiceTable
                .insert {
                    it[this.value] = amount.value
                    it[this.currency] = amount.currency.toString()
                    it[this.status] = status.toString()
                    it[this.customerId] = customer.id
                } get InvoiceTable.id
        }
        return fetchInvoice(id!!)
    }

    fun fetchCustomer(id: Int): Customer? {
        return transaction(db) {
            CustomerTable
                .select { CustomerTable.id.eq(id) }
                .firstOrNull()
                ?.toCustomer()
        }
    }

    fun fetchCustomers(): List<Customer> {
        return transaction(db) {
            CustomerTable
                .selectAll()
                .map { it.toCustomer() }
        }
    }

    fun createCustomer(currency: Currency, membership: MembershipType): Customer? {
        val id = transaction(db) {
            // Insert the customer and return its new id.
            CustomerTable.insert {
                it[this.currency] = currency.toString()
                it[this.membership] = membership.toString()
            } get CustomerTable.id
        }
        return fetchCustomer(id!!)
    }
}

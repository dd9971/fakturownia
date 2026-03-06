package pl.fakturownia.bridge.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.fakturownia.bridge.dto.InvoiceStatusResponse
import pl.fakturownia.bridge.service.FakturowniaInvoiceService
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.mockito.BDDMockito.given
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [InvoiceController])
@Import([ApiErrorHandler])
class InvoiceControllerIntegrationSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockBean
    FakturowniaInvoiceService invoiceService

    def "should create invoice and return fakturownia + ksef ids"() {
        given:
        given(invoiceService.createInvoice(any()))
                .willReturn(new InvoiceStatusResponse(123L, "KSEF-ABC-123", "issued", "ok"))

        when:
        def result = mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content('''
                {
                  "sellerName": "Wystawca Sp. z o.o.",
                  "sellerTaxNo": "6272616681",
                  "buyerName": "Klient Sp. z o.o.",
                  "buyerTaxNo": "6272616681",
                  "positions": [
                    {
                      "name": "Usługa",
                      "quantity": 1,
                      "tax": "23",
                      "totalPriceGross": 123.00
                    }
                  ]
                }
                '''))

        then:
        result.andExpect(status().isCreated())
                .andExpect(jsonPath('$.fakturowniaInvoiceId').value(123))
                .andExpect(jsonPath('$.ksefInvoiceId').value("KSEF-ABC-123"))
                .andExpect(jsonPath('$.invoiceStatus').value("issued"))
                .andExpect(jsonPath('$.ksefStatus').value("ok"))
    }

    def "should return invoice and ksef status for existing invoice"() {
        given:
        given(invoiceService.getInvoiceStatus(123L))
                .willReturn(new InvoiceStatusResponse(123L, "KSEF-XYZ-999", "paid", "processing"))

        expect:
        mockMvc.perform(get("/api/invoices/123/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.fakturowniaInvoiceId').value(123))
                .andExpect(jsonPath('$.ksefInvoiceId').value("KSEF-XYZ-999"))
                .andExpect(jsonPath('$.invoiceStatus').value("paid"))
                .andExpect(jsonPath('$.ksefStatus').value("processing"))
    }

    def "should return invoice pdf"() {
        given:
        byte[] pdfContent = "%PDF-1.4 sample".getBytes("UTF-8")
        given(invoiceService.getInvoicePdf(123L)).willReturn(pdfContent)

        expect:
        mockMvc.perform(get("/api/invoices/123/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "inline; filename=invoice-123.pdf"))
                .andExpect(content().bytes(pdfContent))
    }

    def "should return 400 for invalid create request payload"() {
        when:
        def result = mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content('''
                {
                  "sellerName": "",
                  "sellerTaxNo": "6272616681",
                  "buyerName": "Klient Sp. z o.o.",
                  "buyerTaxNo": "6272616681",
                  "positions": []
                }
                '''))

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.message').value("Niepoprawne dane wejściowe"))
    }
}

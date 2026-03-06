# Fakturownia API – pełna ściąga pól do wystawiania faktury (z KSeF)

> Cel: jedna lista „co mogę uzupełnić przez API”, bez przeklikiwania portalu.
> 
> Zakres oparty o `README.md` (sekcja Faktury + pola) oraz `KSeF.md`.

## 1) Endpointy używane przy wystawianiu

- `POST /invoices.json` – utworzenie faktury.
- `PUT /invoices/{id}.json` – aktualizacja faktury (w tym pozycje, odbiorcy, wystawcy).
- `GET /invoices/{id}.json` – podgląd statusów i pól technicznych (w tym KSeF).

---

## 2) Parametry top-level request (poza `invoice`)

| Pole | Typ | Opis |
|---|---|---|
| `api_token` | string | Token API (wymagany). |
| `invoice` | object | Właściwy obiekt dokumentu. |
| `fill_default_descriptions` | boolean | Dodaje domyślne uwagi z ustawień konta. |
| `identify_oss` | string (`"1"`) | Weryfikacja warunków OSS przed oznaczeniem faktury jako OSS. |
| `gov_save_and_send` | boolean | Zapis i jednoczesne wysłanie faktury do KSeF. |
| `send_to_ksef` | string/bool | Wysłanie istniejącej faktury do KSeF (używane np. przy GET faktury). |

---

## 3) `invoice` – pola główne dokumentu

## 3.1 Identyfikacja i typ dokumentu

- `number`
- `kind` (`vat`, `proforma`, `bill`, `receipt`, `advance`, `final`, `correction`, `invoice_other`, `vat_margin`, `kp`, `kw`, `estimate`, `vat_mp`, `vat_rr`, `correction_note`, `accounting_note`, `client_order`, `dw`, `wnt`, `wdt`, `import_service`, `import_service_eu`, `import_products`, `export_products`)
- `income` (`1` przychodowa / `0` kosztowa)
- `status` (`issued`, `sent`, `paid`, `partial`, `rejected`)
- `oid`, `oid_unique`
- `category_id`

## 3.2 Daty i płatność

- `issue_date`
- `sell_date`
- `payment_to_kind`
- `payment_to`
- `paid`
- `payment_type` (`transfer`, `card`, `cash`, `barter`, `cheque`, `bill_of_exchange`, `cash_on_delivery`, `compensation`, `letter_of_credit`, `payu`, `paypal`, `off`, własny tekst)
- `delivery_date` (wydatki)
- `accounting_vat_tax_date`
- `accounting_income_tax_date`

## 3.3 Sprzedawca (seller)

- `department_id`
- `seller_name`
- `seller_tax_no`
- `seller_tax_no_kind` (`""`, `nip_ue`, `other`, `empty`)
- `seller_bank_account`
- `seller_bank`
- `seller_post_code`
- `seller_city`
- `seller_street`
- `seller_country`
- `seller_email`
- `seller_www`
- `seller_fax`
- `seller_phone`
- `seller_person`
- `seller_bdo_no`
- `seller_jst` (wydatki)
- `seller_gv` (wydatki)

## 3.4 Nabywca (buyer)

- `client_id`
- `buyer_name`
- `buyer_first_name`
- `buyer_last_name`
- `buyer_company`
- `buyer_tax_no`
- `buyer_tax_no_kind` (`""`, `nip_ue`, `other`, `empty`)
- `disable_tax_no_validation`
- `buyer_post_code`
- `buyer_city`
- `buyer_street`
- `buyer_country`
- `buyer_note`
- `buyer_email`
- `buyer_phone`
- `buyer_mobile_phone`
- `buyer_person`
- `buyer_jst` (przychody)
- `buyer_gv` (przychody)
- `buyer_override` (nadpisanie danych klienta przy `client_id`)

## 3.5 Odbiorca i wystawca na fakturze

### Odbiorca (pojedynczy)
- `recipient_id`
- `recipient_name`
- `recipient_street`
- `recipient_post_code`
- `recipient_city`
- `recipient_country`
- `recipient_email`
- `recipient_phone`
- `recipient_note`

### Wielu odbiorców / wystawców (tablice)
- `recipients[]` (obiekty)
- `issuers[]` (obiekty)

Przykładowe pola obiektu `recipient` / `issuer`:
- `id`
- `name`
- `company`
- `email`
- `tax_no`
- `tax_no_kind`
- `_destroy` (przy usuwaniu przez update)

## 3.6 Waluta, kursy, szablon i opisy

- `currency`
- `lang` (w tym warianty dwujęzyczne, np. `pl/en`)
- `use_exchange_currency_rate`
- `exchange_currency`
- `exchange_kind` (`ecb`, `nbp`, `cbr`, `nbu`, `nbg`, `own`)
- `exchange_currency_rate`
- `exchange_note`
- `invoice_template_id`
- `description`
- `description_footer`
- `description_long`
- `internal_note`

## 3.7 Magazyn i logika dokumentu

- `warehouse_id`
- `exclude_from_stock_level`
- `invoice_id` (powiązany dokument)
- `from_invoice_id` (dokument źródłowy)
- `copy_invoice_from` (w praktyce używane przy tworzeniu podobnej faktury)
- `additional_params` (np. `for_receipt`)
- `procedure_designations[]` (`SW`, `EE`, `TP`, `TT_WNT`, `TT_D`, `MR_T`, `MR_UZ`, `I_42`, `I_63`, `B_SPV`, `B_SPV_DOSTAWA`, `B_MPV_PROWIZJA`, `MPP`)

## 3.8 Podatki i oznaczenia

- `additional_info`
- `additional_info_desc`
- `show_discount`
- `discount_kind` (`percent_unit`, `percent_unit_gross`, `percent_total`, `amount`)
- `split_payment`
- `reverse_charge`
- `exempt_tax_kind` (wymagane przy stawce `zw`)
- `np_tax_kind` (wymagane przy stawce `np`)
- `accounting_kind` (wydatki: `purchases`, `expenses`, `media`, `salary`, `incident`, `fuel0`, `fuel_expl75`, `fuel_expl100`, `fixed_assets`, `fixed_assets50`, `no_vat_deduction`)
- `procedure_vat_margin` (wymagane dla `vat_margin`)
- `use_oss`

## 3.9 Korekty i noty

- `correction_reason`
- `corrected_content_before`
- `corrected_content_after`
- `accounting_note_kind` (`credit` / `debit`)
- `gov_corrected_invoice_number` (korekta KSeF)

## 3.10 Inne pola biznesowe

- `place`
- `additional_invoice_field`
- `use_invoice_issuer`
- `invoice_issuer`
- `show_attachments`
- `use_prices_from_price_lists`
- `price_list_id`
- `payment_to_kind`
- `skonto_active`
- `skonto_discount_date`
- `skonto_discount_value`
- `buyer_company`

---

## 4) `invoice.positions[]` – pola pozycji faktury

- `id` (wymagane przy edycji/usuwaniu istniejącej pozycji)
- `product_id`
- `name`
- `additional_info`
- `discount_percent`
- `discount`
- `quantity`
- `quantity_unit`
- `price_net`
- `tax` (np. `23`, `zw`, `np`, itp.)
- `price_gross`
- `total_price_net`
- `total_price_gross`
- `description`
- `code`
- `gtu_code` (np. `GTU_01` ... `GTU_13`)
- `lump_sum_tax`
- `_destroy` (usunięcie pozycji)
- `kind` (w kontekście korekt)
- `correction_before_attributes` (dla korekty)
- `correction_after_attributes` (dla korekty)

---

## 5) `calculating_strategy`

Można przekazać strategię wyliczeń:

- `calculating_strategy.position`: `default` / `keep_gross`
- `calculating_strategy.sum`: `sum` / `keep_gross` / `keep_net`
- `calculating_strategy.invoice_form_price_kind`: `net` / `gross`

---

## 6) KSeF – wymagania i dodatkowe pola

## 6.1 Najważniejsze pola wymagane przy aktywnym KSeF

### Sprzedawca
- `seller_tax_no`
- `seller_name`
- `seller_street`
- `seller_post_code`
- `seller_city`
- `seller_country`

### Nabywca
- `buyer_company`
- `buyer_name` (lub dla osoby fizycznej: `buyer_first_name` + `buyer_last_name`)
- `buyer_tax_no` (gdy `buyer_company=true` i `buyer_tax_no_kind != "empty"`)
- `buyer_tax_no_kind`
- `buyer_country`

## 6.2 Pola warunkowe KSeF

- `exempt_tax_kind` – gdy pozycje mają `tax=zw`
- `np_tax_kind` – gdy pozycje mają `tax=np`

## 6.3 Tax number kind (KSeF)

Dla `buyer_tax_no_kind` (oraz analogicznie `seller_tax_no_kind`, `issuer.tax_no_kind`, `recipient.tax_no_kind`):
- `""` (domyślnie polski NIP)
- `nip_ue`
- `other`
- `empty`

## 6.4 Pola statusowe KSeF w odpowiedziach API

- `gov_status`
- `gov_id` (numer KSeF)
- `gov_send_date`
- `gov_sell_date`
- `gov_error_messages`
- `gov_verification_link`
- `gov_link`
- `gov_corrected_invoice_number`

## 6.5 Dodatkowe zachowania istotne przy KSeF

- Jeśli podajesz `department_id` / `client_id`, dane z tych obiektów nadpisują pola sprzedawcy/nabywcy.
- Przy aktywnym KSeF system może uzupełniać brakujące dane po dopasowaniu `seller_name` / `buyer_name`.
- Po skutecznym nadaniu numeru KSeF (`gov_id`) edycja/usunięcie faktury jest blokowane (prod).

---

## 7) Praktyczna minimalna checklista „co wysyłać zawsze”

Dla stabilnej integracji (portal-independent) warto zawsze przekazywać co najmniej:

1. `kind`, `issue_date`, `sell_date`, `payment_to_kind` lub `payment_to`
2. pełne dane sprzedawcy (`seller_*`) albo `department_id`
3. pełne dane nabywcy (`buyer_*`) albo `client_id`
4. jawnie `buyer_company`
5. `positions[]` z podatkiem (`tax`) i wartościami (`quantity`, `total_price_gross`/`price_net`)
6. przy `zw` → `exempt_tax_kind`, przy `np` → `np_tax_kind`
7. jeżeli używasz KSeF: obsługę pól `gov_*` i retry wysyłki


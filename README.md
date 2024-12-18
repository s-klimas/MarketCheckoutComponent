# MarketCheckoutComponent
Komponent, który kalkuluje ostateczne ceny produktów. 

## Build
Projekt posiada 2 konfiguracje, `dev` do używania podczas programowania oraz `prod` do uruchomienia kodu na produkcji. Jednak przy tak małym komponencie nie różnią się one od siebie. W ramach dobrych praktyk oraz przyszłości, w której podział może być użyteczny zostawiam przygotowaną tak konfiguracje.

## Endpoint
Kontroler obsługuje tylko jeden `POST` endpoint (`/checkout`). Który w requeście przyjmuje listę produktów, a zwraca podsumowany koszyk, z listą produktów z aktualną ceną, sumaryczną kwotę za koszyk, oraz informacje o pakietach i ich zniżkach.

## Produkt
Struktura produktu:
```java
    private Long id; // obowiązkowy
    private String name; // obowiązkowy
    private BigDecimal regularPrice; // obowiązkowy
    private Boolean isMultiPrice = false; // opcjonalny, ustawić na true, w razie wysyłania produktów multiprice
    private Boolean isBundle = false; // opcjonalny, ustawić na true, w razie wysyłania produktów z pakietem

    private BigDecimal specialPrice; // obowiązkowy, gdy isMultiPrice == true
    private Integer requiredQuantity; // obowiązkowy, gdy isMultiPrice == true
    private Long bundleWith; // obowiązkowy, gdy isBundle == true
    private BigDecimal bundleDiscount; // obowiązkowy, gdy isBundle == true
```

## Opis działania
Komponent działa w 6 krokach.

    1. Pogrupowanie produktów.
    2. Zmapowanie na wewnętrzny model `Product` do operacji na nim.
    3. Utworzenie pakietów.
    4. Zmapowanie do ostatecznego modelu DTO `FinalProduct` z ustawieniem poprawnych cen.
    5. Wyliczenie zniżki z pakietów.
    6. Zsumowanie cen produktów oraz zwrócenie obiektu CartDto z przekalkulowanymi danymi.

## Moje wybory
### Model
Zdecydowałem się na jeden model produktu, który zawiera wszystkie możliwe pola, z możliwością zostawienia w niektórych polach `null`. Uważam, że to dobre rozwiązanie, ponieważ nadaje przejrzystości i upraszcza czytanie kodu, w porównaniu z wersją, w której korzystam z dziedziczenia.

## Przykładowy JSON
```JSON
[
  {
    "id": 1,
    "name": "Mleko",
    "regularPrice": 9.99
  },
  {
    "id": 2,
    "name": "Lego",
    "regularPrice": 29.99,
    "isMultiPrice": true,
    "specialPrice": 25.99,
    "requiredQuantity": 3
  },
  {
    "id": 2,
    "name": "Lego",
    "regularPrice": 29.99,
    "isMultiPrice": true,
    "specialPrice": 25.99,
    "requiredQuantity": 3
  },
  {
    "id": 2,
    "name": "Lego",
    "regularPrice": 29.99,
    "isMultiPrice": true,
    "specialPrice": 25.99,
    "requiredQuantity": 3
  },
  {
    "id": 2,
    "name": "Lego",
    "regularPrice": 29.99,
    "isMultiPrice": true,
    "specialPrice": 25.99,
    "requiredQuantity": 3
  },
  {
    "id": 2,
    "name": "Lego",
    "regularPrice": 29.99,
    "isMultiPrice": true,
    "specialPrice": 25.99,
    "requiredQuantity": 3
  },
  {
    "id": 3,
    "name": "Płatki śniadaniowe",
    "regularPrice": 14.99,
    "isBundle": true,
    "bundleWith": 1,
    "bundleDiscount": 0.50
  },
  {
    "id": 4,
    "name": "Konsola",
    "regularPrice": 1499.99,
    "isBundle": true,
    "bundleWith": 5,
    "bundleDiscount": 1.00
  },
  {
    "id": 4,
    "name": "Konsola",
    "regularPrice": 1499.99,
    "isBundle": true,
    "bundleWith": 5,
    "bundleDiscount": 1.00
  },
  {
    "id": 5,
    "name": "Gra komputerowa",
    "regularPrice": 59.99,
    "isMultiPrice": true,
    "isBundle": true,
    "specialPrice": 49.99,
    "requiredQuantity": 2,
    "bundleWith": 4,
    "bundleDiscount": 1.00
  },
  {
    "id": 5,
    "name": "Gra komputerowa",
    "regularPrice": 59.99,
    "isMultiPrice": true,
    "isBundle": true,
    "specialPrice": 49.99,
    "requiredQuantity": 2,
    "bundleWith": 4,
    "bundleDiscount": 1.00
  },
  {
    "id": 5,
    "name": "Gra komputerowa",
    "regularPrice": 59.99,
    "isMultiPrice": true,
    "isBundle": true,
    "specialPrice": 49.99,
    "requiredQuantity": 2,
    "bundleWith": 4,
    "bundleDiscount": 1.00
  },
  {
    "id": 5,
    "name": "Gra komputerowa",
    "regularPrice": 59.99,
    "isMultiPrice": true,
    "isBundle": true,
    "specialPrice": 49.99,
    "requiredQuantity": 2,
    "bundleWith": 4,
    "bundleDiscount": 1.00
  },
  {
    "id": 5,
    "name": "Gra komputerowa",
    "regularPrice": 59.99,
    "isMultiPrice": true,
    "isBundle": true,
    "specialPrice": 49.99,
    "requiredQuantity": 2,
    "bundleWith": 4,
    "bundleDiscount": 1.00
  }
]
```

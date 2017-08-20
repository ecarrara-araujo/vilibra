package br.eng.ecarrara.vilibra.data;

import com.google.gson.Gson;

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;

public class BookVolumeTestDataFactory {

    public static final String TEST_JSON_MEMORIAS_POSTUMAS_QUERY = "{\n" +
            "   \"kind\": \"books#volume\",\n" +
            "   \"id\": \"0C5CBAAAQBAJ\",\n" +
            "   \"etag\": \"K6nyceA1SX0\",\n" +
            "   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/0C5CBAAAQBAJ\",\n" +
            "   \"volumeInfo\": {\n" +
            "    \"title\": \"Memórias Póstumas de Brás Cubas\",\n" +
            "    \"authors\": [\n" +
            "     \"Machado De Assis\"\n" +
            "    ],\n" +
            "    \"publisher\": \"e-artnow sro\",\n" +
            "    \"publishedDate\": \"2013-02-08\",\n" +
            "    \"description\": \"Sobre a obra: Memórias Póstumas de Brás Cubas faz parte da \\\"trilogia realista\\\" de Machado de Assis. O romance foi inicialmente escrito como folhetim, de março a dezembro de 1880, na Revista Brasileira e publicado como livro no ano seguinte pela editora Tipografia Nacional. O livro retrata a escravidão, as classes sociais, o cientificismo e o positivismo da época. É notado como uma das obras mais revolucionárias e inovadoras da literatura brasileira. Sobre Machado de Assis: Joaquim Maria Machado de Assis ( 1839 - 1908) foi um escritor brasileiro, considerado o maior nome da cultura nacional. Escreveu em praticamente todos os gêneros literários e é considerado o introdutor do Realismo no Brasil, com a publicação de Memórias Póstumas de Brás Cubas (1881). A crítica moderna chama de trilogia realista os três romances que marcaram um novo estilo na obra de Machado de Assis, Memórias Póstumas de Brás Cubas (1881), Quincas Borba (1891) e Dom Casmurro (1899), e que decisivamente também inovaram a literatura brasileira, introduzindo o Realismo no Brasil e precedendo outros elementos da literatura contemporânea. Hoje Machado de Assis é considerado um dos grandes gênios da história da literatura, ao lado de autores como Dante, Shakespeare e Camões.\",\n" +
            "    \"industryIdentifiers\": [\n" +
            "     {\n" +
            "      \"type\": \"ISBN_13\",\n" +
            "      \"identifier\": \"9788074840203\"\n" +
            "     },\n" +
            "     {\n" +
            "      \"type\": \"ISBN_10\",\n" +
            "      \"identifier\": \"8074840204\"\n" +
            "     }\n" +
            "    ],\n" +
            "    \"readingModes\": {\n" +
            "     \"text\": true,\n" +
            "     \"image\": true\n" +
            "    },\n" +
            "    \"printType\": \"BOOK\",\n" +
            "    \"categories\": [\n" +
            "     \"Fiction\"\n" +
            "    ],\n" +
            "    \"averageRating\": 4.0,\n" +
            "    \"ratingsCount\": 44,\n" +
            "    \"maturityRating\": \"NOT_MATURE\",\n" +
            "    \"allowAnonLogging\": false,\n" +
            "    \"contentVersion\": \"1.1.1.0.preview.3\",\n" +
            "    \"imageLinks\": {\n" +
            "     \"smallThumbnail\": \"http://books.google.com/books/content?id=0C5CBAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api\",\n" +
            "     \"thumbnail\": \"http://books.google.com/books/content?id=0C5CBAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api\"\n" +
            "    },\n" +
            "    \"language\": \"pt\",\n" +
            "    \"previewLink\": \"http://books.google.com/books?id=0C5CBAAAQBAJ&printsec=frontcover&dq=isbn:8074840204&hl=&cd=1&source=gbs_api\",\n" +
            "    \"infoLink\": \"http://books.google.com/books?id=0C5CBAAAQBAJ&dq=isbn:8074840204&hl=&source=gbs_api\",\n" +
            "    \"canonicalVolumeLink\": \"http://books.google.com/books/about/Mem%C3%B3rias_P%C3%B3stumas_de_Br%C3%A1s_Cubas.html?hl=&id=0C5CBAAAQBAJ\"\n" +
            "   },\n" +
            "   \"saleInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"saleability\": \"FOR_SALE\",\n" +
            "    \"isEbook\": true,\n" +
            "    \"listPrice\": {\n" +
            "     \"amount\": 0.67,\n" +
            "     \"currencyCode\": \"USD\"\n" +
            "    },\n" +
            "    \"retailPrice\": {\n" +
            "     \"amount\": 0.67,\n" +
            "     \"currencyCode\": \"USD\"\n" +
            "    },\n" +
            "    \"buyLink\": \"http://books.google.com/books?id=0C5CBAAAQBAJ&dq=isbn:8074840204&hl=&buy=&source=gbs_api\",\n" +
            "    \"offers\": [\n" +
            "     {\n" +
            "      \"finskyOfferType\": 1,\n" +
            "      \"listPrice\": {\n" +
            "       \"amountInMicros\": 670000.0,\n" +
            "       \"currencyCode\": \"USD\"\n" +
            "      },\n" +
            "      \"retailPrice\": {\n" +
            "       \"amountInMicros\": 670000.0,\n" +
            "       \"currencyCode\": \"USD\"\n" +
            "      }\n" +
            "     }\n" +
            "    ]\n" +
            "   },\n" +
            "   \"accessInfo\": {\n" +
            "    \"country\": \"US\",\n" +
            "    \"viewability\": \"PARTIAL\",\n" +
            "    \"embeddable\": true,\n" +
            "    \"publicDomain\": false,\n" +
            "    \"textToSpeechPermission\": \"ALLOWED\",\n" +
            "    \"epub\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com/books/download/Mem%C3%B3rias_P%C3%B3stumas_de_Br%C3%A1s_Cubas-sample-epub.acsm?id=0C5CBAAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"pdf\": {\n" +
            "     \"isAvailable\": true,\n" +
            "     \"acsTokenLink\": \"http://books.google.com/books/download/Mem%C3%B3rias_P%C3%B3stumas_de_Br%C3%A1s_Cubas-sample-pdf.acsm?id=0C5CBAAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api\"\n" +
            "    },\n" +
            "    \"webReaderLink\": \"http://books.google.com/books/reader?id=0C5CBAAAQBAJ&hl=&printsec=frontcover&output=reader&source=gbs_api\",\n" +
            "    \"accessViewStatus\": \"SAMPLE\",\n" +
            "    \"quoteSharingAllowed\": false\n" +
            "   },\n" +
            "   \"searchInfo\": {\n" +
            "    \"textSnippet\": \"Sobre a obra: Memórias Póstumas de Brás Cubas faz parte da &quot;trilogia realista&quot; de Machado de Assis.\"\n" +
            "   }\n" +
            "  }";

    /**
     * Create a test BookVolume based in a local json String retrieved from Google Books
     * using the following url: https://www.googleapis.com/books/v1/volumes?q=isbn:8074840204
     * @return
     */
    public static final JsonBookVolume getTestBookVolume() {
        Gson gson = new Gson();
        JsonBookVolume bookVolume = gson.fromJson(TEST_JSON_MEMORIAS_POSTUMAS_QUERY, JsonBookVolume.class);
        return bookVolume;
    }

}

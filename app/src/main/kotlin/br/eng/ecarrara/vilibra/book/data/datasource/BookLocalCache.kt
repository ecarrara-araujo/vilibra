package br.eng.ecarrara.vilibra.book.data.datasource

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.core.data.RxCache

interface BookLocalCache : RxCache<String, Book>
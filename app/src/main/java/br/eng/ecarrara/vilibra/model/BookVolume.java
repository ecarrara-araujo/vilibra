package br.eng.ecarrara.vilibra.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class BookVolume {

    @Expose
    private String id;
    @Expose
    private BookVolumeInfo volumeInfo;

    public BookVolume() {
        this.id = "";
        this.volumeInfo = new BookVolumeInfo();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookVolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(BookVolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    /**
     * If the industry identifiers (isbn10 and isbn 13) are the same that should mean that
     * we are talking about the same volumes.
     * @param other BookVolume to check the IndustryIdentifiers against.
     * @return
     */
    public boolean hasSameIndustryIdentifiers(BookVolume other) {
        boolean result = false;
        if(null != other) {
            List<IndustryIdentifier> otherIndustryIdentifiers =
                    other.getVolumeInfo().getIndustryIdentifiers();
            result = this.getVolumeInfo().getIndustryIdentifiers().equals(otherIndustryIdentifiers);
        }
        return result;
    }

    public class BookVolumeInfo {
        @Expose
        private String title;
        @Expose
        private String subtitle;
        @Expose
        private List<String> authors = new ArrayList<>();
        @Expose
        private String publisher;
        @Expose
        private String publishedDate;
        @Expose
        private Integer pageCount;
        @Expose
        private List<IndustryIdentifier> industryIdentifiers = new ArrayList<>();

        public BookVolumeInfo() {
            this.title = "";
            this.subtitle = "";
            this.authors = new ArrayList<>();
            this.publisher = "";
            this.publishedDate = "";
            this.pageCount = 0;
            this.industryIdentifiers = new ArrayList<>();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            if(null == this.subtitle) { this.subtitle = ""; }
            return this.subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getPublishedDate() {
            return publishedDate;
        }

        public void setPublishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
        }

        public Integer getPageCount() {
            if (null == this.pageCount) { this.pageCount = new Integer(0); }
            return this.pageCount;
        }

        public void setPageCount(Integer pageCount) {
            this.pageCount = pageCount;
        }

        public List<IndustryIdentifier> getIndustryIdentifiers() {
            return industryIdentifiers;
        }

        public void setIndustryIdentifiers(List<IndustryIdentifier> industryIdentifiers) {
            this.industryIdentifiers = industryIdentifiers;
        }

        public String getAuthorsAsSemicolonsSeparatedList() {
            String authors = "";

            for (int i = 0; i < this.authors.size(); i++) {
                authors += this.authors.get(i);
                if(i != this.authors.size() - 1) {
                    authors += "; ";
                }
            }

            return authors;
        }

        public String getISBN10() {
            String isbn10 = getIndustryIdentifierByType(IndustryIdentifier.BOOK_ISBN_TYPE_10)
                    .getIdentifier();
            return isbn10;
        }

        public String getISBN13() {
            String isbn13 = getIndustryIdentifierByType(IndustryIdentifier.BOOK_ISBN_TYPE_13)
                    .getIdentifier();
            return isbn13;
        }

        private IndustryIdentifier getIndustryIdentifierByType(final String type) {
            IndustryIdentifier industryIdentifier = null;

            if(TextUtils.isEmpty(type)) {
                return industryIdentifier;
            }

            for (int j = 0; j < industryIdentifiers.size(); j++) {
                IndustryIdentifier tempIndustryIdentifier = industryIdentifiers.get(j);
                if(type.equals(tempIndustryIdentifier.getType())) {
                    industryIdentifier = tempIndustryIdentifier;
                    break;
                }
            }

            return industryIdentifier;
        }
    }

    public class IndustryIdentifier {
        public static final String BOOK_ISBN_TYPE_10 = "ISBN_10";
        public static final String BOOK_ISBN_TYPE_13 = "ISBN_13";

        @Expose
        private String type;
        @Expose
        private String identifier;

        public IndustryIdentifier() {
            this.type = "";
            this.identifier = "";
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public boolean equals(Object other) {
            boolean result = false;
            if(other instanceof IndustryIdentifier) {
                IndustryIdentifier tempOther = (IndustryIdentifier) other;
                result = (this.type.equals(tempOther.getType())
                        && this.identifier.equals(tempOther.getIdentifier()));
            }
            return result;
        }
    }


}

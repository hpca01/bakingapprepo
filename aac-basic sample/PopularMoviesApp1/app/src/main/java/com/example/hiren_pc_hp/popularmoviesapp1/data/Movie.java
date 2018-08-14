package com.example.hiren_pc_hp.popularmoviesapp1.data;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Movie implements Parcelable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    public final static Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return (new Movie[size]);
        }

    };

    protected Movie(Parcel in) {
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, Result.class.getClassLoader());
    }

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    /**
     * @param results
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public Movie(Integer page, Integer totalResults, Integer totalPages, List<Result> results) {
        super();
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

    public class Result implements Parcelable {

        @SerializedName("vote_count")
        @Expose
        private Integer voteCount;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("video")
        @Expose
        private Boolean video;
        @SerializedName("vote_average")
        @Expose
        private Float voteAverage;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("popularity")
        @Expose
        private Float popularity;
        @SerializedName("poster_path")
        @Expose
        private String posterPath;
        @SerializedName("original_language")
        @Expose
        private String originalLanguage;
        @SerializedName("original_title")
        @Expose
        private String originalTitle;
        @SerializedName("genre_ids")
        @Expose
        private List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        @Expose
        private String backdropPath;
        @SerializedName("adult")
        @Expose
        private Boolean adult;
        @SerializedName("overview")
        @Expose
        private String overview;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;
        public final Parcelable.Creator<Result> CREATOR = new Creator<Result>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            public Result[] newArray(int size) {
                return (new Result[size]);
            }

        };

        protected Result(Parcel in) {
            this.voteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.video = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.voteAverage = ((Float) in.readValue((Float.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.popularity = ((Float) in.readValue((Float.class.getClassLoader())));
            this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
            this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
            this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(this.genreIds, (java.lang.Integer.class.getClassLoader()));
            this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
            this.adult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.overview = ((String) in.readValue((String.class.getClassLoader())));
            this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        }

        /**
         * No args constructor for use in serialization
         */
        public Result() {
        }

        /**
         * @param genreIds
         * @param id
         * @param title
         * @param releaseDate
         * @param overview
         * @param posterPath
         * @param originalTitle
         * @param voteAverage
         * @param originalLanguage
         * @param adult
         * @param backdropPath
         * @param voteCount
         * @param video
         * @param popularity
         */
        public Result(Integer voteCount, Integer id, Boolean video, Float voteAverage, String title, Float popularity, String posterPath, String originalLanguage, String originalTitle, List<Integer> genreIds, String backdropPath, Boolean adult, String overview, String releaseDate) {
            super();
            this.voteCount = voteCount;
            this.id = id;
            this.video = video;
            this.voteAverage = voteAverage;
            this.title = title;
            this.popularity = popularity;
            this.posterPath = posterPath;
            this.originalLanguage = originalLanguage;
            this.originalTitle = originalTitle;
            this.genreIds = genreIds;
            this.backdropPath = backdropPath;
            this.adult = adult;
            this.overview = overview;
            this.releaseDate = releaseDate;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Float getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Float voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Float getPopularity() {
            return popularity;
        }

        public void setPopularity(Float popularity) {
            this.popularity = popularity;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        @Override

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(voteCount);
            dest.writeValue(id);
            dest.writeValue(video);
            dest.writeValue(voteAverage);
            dest.writeValue(title);
            dest.writeValue(popularity);
            dest.writeValue(posterPath);
            dest.writeValue(originalLanguage);
            dest.writeValue(originalTitle);
            dest.writeList(genreIds);
            dest.writeValue(backdropPath);
            dest.writeValue(adult);
            dest.writeValue(overview);
            dest.writeValue(releaseDate);
        }

        public int describeContents() {
            return 0;
        }

    }
}
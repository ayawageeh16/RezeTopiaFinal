package io.krito.com.rezetopia.models.pojo.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("id")
        @Expose
        private Integer id;

        /**
         * No args constructor for use in serialization
         *
         */
        public Media() {
        }

        /**
         *
         * @param id
         * @param path
         */
        public Media(String path, Integer id) {
            super();
            this.path = path;
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

}


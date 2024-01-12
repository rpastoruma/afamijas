    package afamijas.queuemail.model;

    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;

    @Document(collection = "queuemails_attachments")
    public class QueueEmailAttachment
    {
        @Id
        private String _id;

        private String idemail;

        private String filepath;

        private Boolean removeAfterSent;


        public QueueEmailAttachment()
        {
            this.removeAfterSent = false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QueueEmailAttachment that = (QueueEmailAttachment) o;

            return _id.equals(that._id);
        }

        @Override
        public int hashCode() {
            return _id.hashCode();
        }


        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getIdemail() {
            return idemail;
        }

        public void setIdemail(String idemail) {
            this.idemail = idemail;
        }

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public Boolean getRemoveAfterSent() {
            return removeAfterSent;
        }

        public void setRemoveAfterSent(Boolean removeAfterSent) {
            this.removeAfterSent = removeAfterSent;
        }
    }

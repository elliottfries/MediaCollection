package MediaCollection;



import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javafx.collections.transformation.SortedList;

public class MediaItem implements Serializable, Comparable<MediaItem> {
    
    private String title;
    private String format;
    private String loanedTo;
    private Date dateLoaned;
    
    public MediaItem(String title, String format) {
        this.title = title;
        this.format = format;
        this.loanedTo = null;
        this.dateLoaned = null;
    }
    public MediaItem() {
        
    }
    
    public void loan(String loanedTo, Date loanedOn) {
        this.loanedTo = loanedTo;
        this.dateLoaned = loanedOn;
    }
    
    public void returnItem() {
        this.loanedTo = null;
        this.dateLoaned = null;
    }
    public String getName() {
        this.title = title;
        
        return title;
    }
    public void setOnLoan(String title, String format, String person, Date date) {
        this.title = title;
        this.format = format;
        this.loanedTo = person;
        this.dateLoaned = date;
        
    }
    public Date getDate() {
        this.dateLoaned = dateLoaned;

        return dateLoaned;
    }
    
    public String getFormat() {
        this.format = format;
        
        return format;
    }
    
    @Override
    public String toString() {
        String response = title + " - " + format;
        
        if (loanedTo != null) {
            response += " (" + loanedTo + " on " + dateLoaned + ")";
        }
        
        response = response.replace("00:00:00 EDT ", "");
        return response;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.title);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MediaItem other = (MediaItem) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return true;
    }
   
    public String getLoanedTo() { return loanedTo; }

    @Override
    public int compareTo(MediaItem t) {
            
            return (this.title.toString()).compareTo(t.title.toString());         
    }
 
   
}
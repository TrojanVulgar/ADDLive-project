package nemosofts.live.tv.Listltem;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class Listltem {
    private String id;
    private String name;
    private String url;
    private String pay;
    private String imageUrl;
    private String video_type;
    private String total_views;

    public Listltem(String id,String name, String url, String pay, String imageUrl,String video_type, String total_views) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.pay = pay;
        this.imageUrl = imageUrl;
        this.video_type = video_type;
        this.total_views = total_views;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getPay() {
        return pay;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideo_type() {
        return video_type;
    }

    public String getTotal_views() {
        return total_views;
    }


}
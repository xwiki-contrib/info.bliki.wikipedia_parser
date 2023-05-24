package info.bliki.wiki.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.WikipediaScanner;

/**
 * Represents an [[Image:....]] wiki link with all the possible attributes.
 *
 * Copied from Patch #1488331 sf.net user: o_rossmueller; modified by axelclk
 * http ://sourceforge.net/tracker/index.php?func=detail&aid=1488331&group_id=
 * 128886 &atid=713150
 *
 */
public class ImageFormat {
    private static final Set<String> TYPE =
        new HashSet<>(Arrays.asList("border", "frameless", "frame", "framed", "thumb", "thumbnail"));

    private static final Set<String> HORIZONTAL_ALIGNMENT = new HashSet<>(Arrays.asList("left", "right", "none"));

    private static final Set<String> VERTICAL_ALIGNMENT =
        new HashSet<>(Arrays.asList("baseline", "sub", "super", "top", "text-top", "middle", "bottom", "text-bottom"));

    public static ImageFormat getImageFormat(String rawImageLink, String imageNamespace) {
        ImageFormat img = new ImageFormat();
        List<String> list = WikipediaScanner.splitByPipe(rawImageLink, null);
        if (list.size() > 0) {
            String attrValue;
            String token = list.get(0).trim();
            img.setFilename("");
            if (token.length() > imageNamespace.length() && token.charAt(imageNamespace.length()) == ':') {
                if (imageNamespace.equalsIgnoreCase(token.substring(0, imageNamespace.length()))) {
                    img.setFilename(token.substring(imageNamespace.length() + 1));
                    img.setNamespace(imageNamespace);
                }
            }
            String caption;
            for (int j = 1; j < list.size(); j++) {
                caption = list.get(j).trim();
                if (caption.length() > 0) {
                    token = caption.toLowerCase();
                    int defIndex = token.indexOf("=");
                    if (defIndex > 0) {
                        token = token.substring(0, defIndex).trim();
                        if (token.equals("link")) {
                            attrValue = caption.substring(defIndex + 1).trim();
                            img.setLink(attrValue);
                            continue;
                        }
                        if (token.equals("alt")) {
                            attrValue = caption.substring(defIndex + 1).trim();
                            img.setAlt(Encoder.encodeHtml(attrValue));
                            continue;
                        }
                    } else {
                        if (TYPE.contains(token)) {
                            img.setType(token);
                            continue;
                        }

                        if (HORIZONTAL_ALIGNMENT.contains(token)) {
                            img.setHorizontalAlign(token);
                            continue;
                        }

                        if (VERTICAL_ALIGNMENT.contains(token)) {
                            img.setVerticalAlign(token);
                            continue;
                        }

                        if (token.endsWith("px")) {
                            img.setSize(token);
                            continue;
                        }
                    }
                    img.setCaption(caption);
                }
            }
        }
        return img;
    }

    private String fFilename;

    private String fType;

    private String fHorizontalAlign = "none";

    private String fVerticalAlign = "middle";

    private String fWidthStr = null;

    private int fWidth = -1;

    private String fHeightStr = null;

    private int fHeight = -1;

    private String fCaption;

    private String fAlt;

    private String fNamespace;

    private String fLink;

    public String getAlt() {
        return fAlt;
    }

    public String getCaption() {
        return fCaption;
    }

    public String getFilename() {
        return fFilename;
    }

    @Deprecated
    public String getLocation() {
        return getHorizontalAlign();
    }

    /**
     * @since 3.1.1-xwiki
     */
    public String getHorizontalAlign() {
        return fHorizontalAlign;
    }

    /**
     * @since 3.1.1-xwiki
     */
    public String getVerticalAlign() {
        return fVerticalAlign;
    }

    public String getNamespace() {
        return fNamespace;
    }

    /**
     * Get the &quote;link=&quote; attribute from the [[Image:...]] wiki link.
     *
     * @return the &quote;link=&quote; attribute
     */
    public String getLink() {
        return fLink;
    }

    /**
     * Get the width of the image in pixel (example: "600px")
     *
     * @param size
     *
     * @return <code>-1</code> if no width is specified
     */
    public int getWidth() {
        return fWidth;
    }

    /**
     * Get the width of the image as a string
     *
     * @param size
     *
     * @return <code>null</code> if no width is specified
     */
    public String getWidthStr() {
        return fWidthStr;
    }

    /**
     * Get the height of the image in pixel (example: "600px")
     *
     * @param size
     *
     * @return <code>-1</code> if no width is specified
     */
    public int getHeight() {
        return fHeight;
    }

    /**
     * Get the height of the image as a string
     *
     * @param size
     *
     * @return <code>null</code> if no width is specified
     */
    public String getHeightStr() {
        return fHeightStr;
    }

    public String getType() {
        return fType;
    }

    public void setAlt(String alt) {
        fAlt = alt;
    }

    public void setCaption(String caption) {
        this.fCaption = caption;
    }

    public void setFilename(String filename) {
        this.fFilename = filename;
    }

    @Deprecated
    public void setLocation(String location) {
        setHorizontalAlign(location);
    }

    /**
     * @since 3.1.1-xwiki
     */
    public void setHorizontalAlign(String horizontalAlign) {
        this.fHorizontalAlign = horizontalAlign.toLowerCase();
    }

    /**
     * @since 3.1.1-xwiki
     */
    public void setVerticalAlign(String verticalAlign) {
        this.fVerticalAlign = verticalAlign.toLowerCase();
    }

    public void setNamespace(String namespace) {
        this.fNamespace = namespace;
    }

    public void setLink(String link) {
        this.fLink = link;
    }

    /**
     * Set the size of the image in pixel. If the given string ends with "px"
     * additionally calculate the integer value of the width and optionally the
     * size of the height (example: "600px"). If the size is negative ignore it.
     *
     * See <a href="https://en.wikipedia.org/wiki/Image_markup#Size">Image
     * markup#Size</a> and See <a
     * href="https://www.mediawiki.org/wiki/Help:Images">Help:Images</a>
     *
     * @param size
     */
    public void setSize(String size) {
        String sizeStr = size.toLowerCase();
        if (sizeStr.endsWith("px")) {
            int indexOfX = sizeStr.indexOf("x");
            if (indexOfX >= 0 && indexOfX < sizeStr.length() - 1) {
                // format "widthxheightpx"
                fWidthStr = sizeStr.substring(0, indexOfX) + "px";
                fHeightStr = sizeStr.substring(indexOfX + 1);
                try {
                    this.fHeight = Integer.parseInt(fHeightStr.substring(0, fHeightStr.length() - 2));
                    if (fHeight < 0) {
                        this.fHeight = -1;
                        this.fHeightStr = null;
                    }
                } catch (NumberFormatException e) {
                    this.fHeight = -1;
                    this.fHeightStr = null;
                }
            } else {
                fWidthStr = sizeStr;
                fHeightStr = null;
                fHeight = -1;
            }
            try {
                this.fWidth = Integer.parseInt(fWidthStr.substring(0, fWidthStr.length() - 2));
                if (fWidth < 0) {
                    this.fWidth = -1;
                    this.fWidthStr = null;
                }
            } catch (NumberFormatException e) {
                this.fWidth = -1;
                this.fWidthStr = null;
            }
        }
    }

    public void setType(String type) {
        this.fType = type.toLowerCase();
    }

    /**
     * Set the width of the image in pixel. This method is typically used to set a
     * &quot;default width&quot; (220px) for images of type &quot;thumb&quot; if
     * no width is set in the image format string.
     *
     * @param width
     * @see AbstractWikiModel#setDefaultThumbWidth(ImageFormat)
     */
    public void setWidth(int width) {
        fWidth = width;
    }
}

package info.bliki.wiki.filter;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a single row in a wiki table (i.e. table syntax bordered by
 * <code>{| ..... |}</code> ). See: <a
 * href="https://meta.wikimedia.org/wiki/Help:Table">Help - Table</a>
 *
 */
public class WPRow {
    private List<WPCell> fCells;

    private String fParams;

    private int fType;

    private Map<String, String> fAttributes;

    public WPRow(List<WPCell> cells) {
        fCells = cells;
        fParams = null;
        fType = WPCell.DEFAULT;
        fAttributes = Collections.emptyMap();
    }

    public Map<String, String> getAttributes() {
        return fAttributes;
    }

    /**
     * @return Returns the parameters.
     */
    public String getParams() {
        return fParams;
    }

    /**
     * @param params
     *          The params to set.
     */
    public void setParams(String params) {
        this.fParams = params;
        this.fAttributes = Util.getAttributes(params);
        this.fAttributes =
            this.fAttributes != null ? Collections.unmodifiableMap(this.fAttributes) : Collections.emptyMap();
    }

    /**
     * @param o
     * @return
     */
    public boolean add(WPCell cell) {
        return fCells.add(cell);
    }

    /**
     * @param index
     * @return
     */
    public WPCell get(int index) {
        return fCells.get(index);
    }

    /**
     * @return
     */
    public int size() {
        return fCells.size();
    }

    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        if (fCells.size() > 0) {
            if (fType == WPCell.CAPTION) {
                if (fCells.size() == 1) {
                    (fCells.get(0)).renderHTML(converter, buf, wikiModel);
                }
            } else {
                if (HTMLTag.NEW_LINES) {
                    buf.append("\n<tr");
                } else {
                    buf.append("<tr");
                }
                HTMLTag.appendEscapedAttributes(buf, fAttributes);
                buf.append(">");
                WPCell cell;
                for (int i = 0; i < fCells.size(); i++) {
                    cell = fCells.get(i);
                    cell.renderHTML(converter, buf, wikiModel);
                }
                buf.append("</tr>");
            }
        }
    }

    public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        if (fCells.size() > 0) {
            if (fType == WPCell.CAPTION) {
                if (fCells.size() == 1) {
                    if (HTMLTag.NEW_LINES) {
                        buf.append("\n");
                    }
                    (fCells.get(0)).renderPlainText(converter, buf, wikiModel);
                }
            } else {
                if (HTMLTag.NEW_LINES) {
                    buf.append("\n");
                }
                WPCell cell;
                for (int i = 0; i < fCells.size(); i++) {
                    cell = fCells.get(i);
                    cell.renderPlainText(converter, buf, wikiModel);
                    buf.append(" ");
                }
            }
        }
    }

    public int getNumColumns() {
        return fCells.size();
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return fType;
    }

    /**
     * @param type
     *          The type to set.
     */
    public void setType(int type) {
        fType = type;
    }
}

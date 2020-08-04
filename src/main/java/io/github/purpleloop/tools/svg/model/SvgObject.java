package io.github.purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SvgObject {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(SvgObject.class);

    private String id;
    private Style style;
    private SvgObject parent;

    public SvgObject(String id) {
        this.id = id;
    }

    public final String getId() {
        return this.id;
    }

    public abstract void render(Graphics2D g, Stack<Transformation> trans);

    public void setStyle(Style style) {
        this.style = style;
    }

    public final Style getStyle() {
        return style;
    }

    protected String getStyleProperty(String name) {
        return style.getProperty(name);
    }

    public abstract SvgObject selectIn(int x, int y);

    public abstract void move(int dx, int dy);

    public void registerIds(Map<String, SvgObject> mapId) {
        mapId.put(this.id, this);
    }

    /**
     * @return the parent
     */
    public final SvgObject getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public final void setParent(SvgObject parent) {
        this.parent = parent;
    }

    public SvgDefinition resolveUrl(String url) {

        if (parent != null) {
            return parent.resolveUrl(url);
        }
        return null;
    }

}

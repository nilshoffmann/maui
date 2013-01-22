/* 
 * Maui, Maltcms User Interface. 
 * Copyright (C) 2008-2012, The authors of Maui. All rights reserved.
 *
 * Project website: http://maltcms.sf.net
 *
 * Maui may be used under the terms of either the
 *
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 *
 * or the
 *
 * Eclipse Public License (EPL)
 * http://www.eclipse.org/org/documents/epl-v10.php
 *
 * As a user/recipient of Maui, you may choose which license to receive the code 
 * under. Certain files or entire directories may not be covered by this 
 * dual license, but are subject to licenses compatible to both LGPL and EPL.
 * License exceptions are explicitly declared in all relevant files or in a 
 * LICENSE file in the relevant directories.
 *
 * Maui is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. Please consult the relevant license documentation
 * for details.
 */
package net.sf.maltcms.chromaui.chromatogram1Dviewer.api;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
public class GraphicsSettings {

    private Stroke stroke;
    private Composite composite;
    private AffineTransform transform;
    private Color background;
    private Font font;
    private Shape clip;
    private Paint paint;

    public GraphicsSettings(Stroke stroke, Composite composite,
            AffineTransform transform, Paint color, Color background, Font font,
            Shape clip) {
        this.stroke = stroke;
        this.composite = composite;
        this.transform = transform;
        this.background = background;
        this.font = font;
        this.clip = clip;
        this.paint = paint;
    }

    public static GraphicsSettings create(Graphics2D g2) {
        GraphicsSettings gs = new GraphicsSettings(g2.getStroke(), g2.
                getComposite(), g2.getTransform(), g2.getPaint(), g2.
                getBackground(), g2.getFont(), g2.getClip());
        return gs;
    }
    
    public static GraphicsSettings clone(GraphicsSettings gs) {
        GraphicsSettings settings = new GraphicsSettings(gs.getStroke(),gs.getComposite(),gs.getTransform(),gs.getPaint(),gs.getBackground(),gs.getFont(),gs.getClip());
        return settings;
    }

    public void apply(Graphics2D g2) {
        g2.setStroke(stroke);
        g2.setComposite(composite);
        g2.setTransform(transform);
        g2.setBackground(background);
        g2.setFont(font);
        g2.setClip(clip);
        g2.setPaint(paint);
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Shape getClip() {
        return clip;
    }

    public void setClip(Shape clip) {
        this.clip = clip;
    }

    public void setColor(Color color) {
        this.paint = color;
    }

    public Composite getComposite() {
        return composite;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }
}

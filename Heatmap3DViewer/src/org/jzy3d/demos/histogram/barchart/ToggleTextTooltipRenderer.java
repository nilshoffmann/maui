package org.jzy3d.demos.histogram.barchart;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.rendering.tooltips.TextTooltipRenderer;

/**
 *
 * @author ao
 */
class ToggleTextTooltipRenderer extends TextTooltipRenderer {

    private final BarChartBar ad;
    private boolean visible = false;

    public ToggleTextTooltipRenderer(String text, final BarChartBar ad) {
        super(text, new IntegerCoord2d(), ad.getBounds().getCenter());
        this.ad = ad;
    }

//    @Override
//    public void render(Graphics2D g2d) {
//    }
    @Override
    public void render(Graphics2D g2d) {
        if (visible) {
            System.out.println("Setting tooltip");
            updateTargetCoordinate(ad.getBounds().getCenter());
            IntegerCoord2d c2d = ad.getCenterToScreenProj();
            updateScreenPosition(c2d);

            this.text = ad.getInfo();
            Rectangle2D r2 = g2d.getFontMetrics().getStringBounds(text, g2d);
            AffineTransform toCenterTransf = AffineTransform.getTranslateInstance(-r2.getWidth() / 2.0d, -r2.getHeight() / 2.0d);
            Shape r2center = toCenterTransf.createTransformedShape(r2);
            AffineTransform trl = AffineTransform.getTranslateInstance(screenLocation.x, screenLocation.y);
            lastBounds = trl.createTransformedShape(r2).getBounds();
//            lastBounds = new Rectangle(screenLocation.x - 10, screenLocation.y - 13, 10 + text.length() * 6, 16);
            AffineTransform zoom = AffineTransform.getScaleInstance(1.1, 1.1);

            Rectangle textBox;
            try {
                textBox = trl.createTransformedShape(toCenterTransf.createInverse().createTransformedShape(zoom.createTransformedShape(r2center))).getBounds();
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);

                g2d.setComposite(ac.derive(0.9f));
                g2d.setColor(Color.WHITE);
                g2d.fillRect(textBox.x, textBox.y, textBox.width, textBox.height);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(textBox.x, textBox.y, textBox.width, textBox.height);
                g2d.setComposite(ac.derive(1.0f));
                g2d.drawString(text, screenLocation.x, screenLocation.y);
            } catch (NoninvertibleTransformException ex) {
                Logger.getLogger(ToggleTextTooltipRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
//            AffineTransform trl2 = AffineTransform.getTranslateInstance(screenLocation.x-textBox.x, screenLocation.y-textBox.y);
//            textBox = trl2.createTransformedShape(textBox).getBounds();

//            super.render(g2d);
        }

    }

    void setVisible(boolean b) {
        visible = b;
    }
}

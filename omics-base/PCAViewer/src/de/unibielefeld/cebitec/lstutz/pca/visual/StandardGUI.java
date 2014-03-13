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
package de.unibielefeld.cebitec.lstutz.pca.visual;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;
import de.unibielefeld.cebitec.lstutz.pca.data.DataModel;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Geometry;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Raster;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.openide.util.lookup.InstanceContent;

public class StandardGUI extends JPanel implements MouseListener, ActionListener, ItemListener, ClipboardOwner {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimpleUniverse uni;
    private BranchGroup data_branch;
    private Canvas3D can;
    private PickCanvas pickCanvas;
    private HashMap<String, ArrayList<DataModel>> hash;
    private InfoPanel info;
    private Background b;
    private java.util.BitSet visibleNodes;
    private HashMap<String, Integer> address = new HashMap<String, Integer>();
    private Switch switchGroup;
    private DataModel selDa;
    private InstanceContent content;
    private Object selectedPayload;

    public StandardGUI(String title, HashMap<String, ArrayList<DataModel>> hash) {
        this.hash = hash;
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setTitle(title);
        this.setPreferredSize(new Dimension(840, 510));
        this.setLayout(new BorderLayout());
        this.create_universe(hash);
        this.add(BorderLayout.CENTER, can);
        GroupPanel grp = new GroupPanel(hash, this);
        this.add(BorderLayout.SOUTH, grp);
        info = new InfoPanel(hash.get(hash.keySet().toArray()[0]).get(0).getHeading(), this);
        this.add(BorderLayout.EAST, info);
        this.setVisible(true);
//		this.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getSize().width) / 2;
        int y = (screenSize.height - this.getSize().height) / 2;
        this.setLocation(x, y);
    }

    public void setInstanceContent(InstanceContent content) {
        this.content = content;
    }

    private void create_universe(HashMap<String, ArrayList<DataModel>> hash) {
        can = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        can.setPreferredSize(new Dimension(640, 480));
        can.setMinimumSize(new Dimension(320, 280));

        uni = new SimpleUniverse(can);
        uni.getViewingPlatform().setNominalViewingTransform();

        BranchGroup g = new BranchGroup();
        //g.setCapability(Node.ENABLE_PICK_REPORTING);
        LineArray a = new LineArray(30, LineArray.COORDINATES | LineArray.COLOR_3);
        // x achse
        a.setCoordinate(0, new Point3f(-1.0f, 0, 0));
        a.setColor(0, new Color3f(0, 1, 0));
        a.setCoordinate(1, new Point3f(1.0f, 0, 0));
        a.setColor(1, new Color3f(0, 1, 0));
        // y achse
        a.setCoordinate(2, new Point3f(0, -1.0f, 0));
        a.setColor(2, new Color3f(1, 0, 0));
        a.setCoordinate(3, new Point3f(0, 1.0f, 0));
        a.setColor(3, new Color3f(1, 0, 0));
        // z achse
        a.setCoordinate(4, new Point3f(0, 0, -1.0f));
        a.setColor(4, new Color3f(0, 0, 1));
        a.setCoordinate(5, new Point3f(0, 0, 1.0f));
        a.setColor(5, new Color3f(0, 0, 1));
        // pfeil x achse
        a.setCoordinate(6, new Point3f(0.95f, .05f, 0));
        a.setColor(6, new Color3f(0, 1, 0));
        a.setCoordinate(7, new Point3f(1, 0, 0));
        a.setColor(7, new Color3f(0, 1, 0));
        a.setCoordinate(8, new Point3f(0.95f, -.05f, 0));
        a.setColor(8, new Color3f(0, 1, 0));
        a.setCoordinate(9, new Point3f(1, 0, 0));
        a.setColor(9, new Color3f(0, 1, 0));

        a.setCoordinate(18, new Point3f(0.95f, 0, .05f));
        a.setColor(18, new Color3f(0, 1, 0));
        a.setCoordinate(19, new Point3f(1, 0, 0));
        a.setColor(19, new Color3f(0, 1, 0));
        a.setCoordinate(20, new Point3f(0.95f, 0, -.05f));
        a.setColor(20, new Color3f(0, 1, 0));
        a.setCoordinate(21, new Point3f(1, 0, 0));
        a.setColor(21, new Color3f(0, 1, 0));
        // pfeil y achse
        a.setCoordinate(10, new Point3f(.05f, .95f, 0));
        a.setColor(10, new Color3f(1, 0, 0));
        a.setCoordinate(11, new Point3f(0, 1, 0));
        a.setColor(11, new Color3f(1, 0, 0));
        a.setCoordinate(12, new Point3f(-.05f, .95f, 0));
        a.setColor(12, new Color3f(1, 0, 0));
        a.setCoordinate(13, new Point3f(0, 1, 0));
        a.setColor(13, new Color3f(1, 0, 0));

        a.setCoordinate(22, new Point3f(0, .95f, .05f));
        a.setColor(22, new Color3f(1, 0, 0));
        a.setCoordinate(23, new Point3f(0, 1, 0));
        a.setColor(23, new Color3f(1, 0, 0));
        a.setCoordinate(24, new Point3f(0, .95f, -.05f));
        a.setColor(24, new Color3f(1, 0, 0));
        a.setCoordinate(25, new Point3f(0, 1, 0));
        a.setColor(25, new Color3f(1, 0, 0));

        // pfeil z achse
        a.setCoordinate(14, new Point3f(0, .05f, .95f));
        a.setColor(14, new Color3f(0, 0, 1));
        a.setCoordinate(15, new Point3f(0, 0, 1));
        a.setColor(15, new Color3f(0, 0, 1));
        a.setCoordinate(16, new Point3f(0, -.05f, .95f));
        a.setColor(16, new Color3f(0, 0, 1));
        a.setCoordinate(17, new Point3f(0, 0, 1));
        a.setColor(17, new Color3f(0, 0, 1));

        a.setCoordinate(26, new Point3f(.05f, 0, .95f));
        a.setColor(26, new Color3f(0, 0, 1));
        a.setCoordinate(27, new Point3f(0, 0, 1));
        a.setColor(27, new Color3f(0, 0, 1));
        a.setCoordinate(28, new Point3f(-.05f, 0, .95f));
        a.setColor(28, new Color3f(0, 0, 1));
        a.setCoordinate(29, new Point3f(0, 0, 1));
        a.setColor(29, new Color3f(0, 0, 1));
        //hinzufügen
        g.addChild(new Shape3D(a));

        b = new Background(new Color3f(.20f, .20f, .20f));
        b.setApplicationBounds(new BoundingSphere(new Point3d(), 1000.0));
        b.setCapability(Background.ALLOW_COLOR_WRITE);
        g.addChild(b);

        pickCanvas = new PickCanvas(can, g);
        pickCanvas.setMode(PickCanvas.GEOMETRY);
        can.addMouseListener(this);

        AmbientLight lightA = new AmbientLight();
        lightA.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 15));
        g.addChild(lightA);

        DirectionalLight lightD1 = new DirectionalLight();
        lightD1.setDirection(new Vector3f(-1, -1, -1));
        lightD1.setInfluencingBounds(new BoundingSphere(new Point3d(1, 1, 1), 15));
        g.addChild(lightD1);

        data_branch = new BranchGroup();
        data_branch.setCapability(BranchGroup.ALLOW_DETACH);
        data_branch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        data_branch.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        data_branch.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

        this.display_data(hash);

        g.addChild(data_branch);

        g.compile();

        OrbitBehavior orbit = new OrbitBehavior(can, OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM);
        BoundingSphere bound = new BoundingSphere(new Point3d(0, 0, 0), 15);
        orbit.setSchedulingBounds(bound);
        orbit.setMinRadius(1.5);
        uni.getViewingPlatform().setViewPlatformBehavior(orbit);
        Transform3D transform = new Transform3D();
        transform.setTranslation(new Vector3f(0, 0, 3.5f));
        uni.getViewingPlatform().getViewPlatformTransform().setTransform(transform);
        //this.setPreferredSize(new Dimension(640,480));
        uni.addBranchGraph(g);
    }

    private void display_data(HashMap<String, ArrayList<DataModel>> hash) {
        //create the Switch Node
        switchGroup = new Switch(Switch.CHILD_MASK);
        switchGroup.setCapability(Switch.ALLOW_SWITCH_WRITE);
        Integer index = 0;
        for (String s : hash.keySet()) {

            ArrayList<DataModel> list = hash.get(s);

            BranchGroup swbranch = new BranchGroup();

            double max = Double.NEGATIVE_INFINITY;
            for (DataModel data : list) {
                for (Double d : data.getCoordinates()) {
                    if (d > max) {
                        max = d;
                    }
                    if (d < 0 && (d * -1) > max) {
                        max = (d * -1);
                    }
                }
            }
            //System.out.println("Einträge: "+list.size());
            for (DataModel d : list) {
                d.setFactor(max);
                ArrayList<Double> coords = d.getCoordinates();

                Appearance app = new Appearance();
                app.setColoringAttributes(new ColoringAttributes(0, 0, 0, 0));

                Material mat = new Material();
                mat.setDiffuseColor(d.getColor());
                System.out.println(d.getColor());
                Color3f ambient = new Color3f(d.getColor());
                ambient.scale(.7f);
                System.out.println(ambient);
                mat.setAmbientColor(ambient);
                app.setMaterial(mat);

                Primitive object = new Sphere(.02f, Sphere.GENERATE_NORMALS, app);
                if (d.getShape() == null || d.getShape().equals("s")) {
                    object = new Sphere(.02f, Sphere.GENERATE_NORMALS, app);
                } else if (d.getShape().equals("b")) {
                    object = new Box(.02f, .02f, .02f, Box.GENERATE_NORMALS, app);
                } else if (d.getShape().equals("l")) {
                    if (coords.get(0).compareTo(Double.NaN) != 0
                        && coords.get(1).compareTo(Double.NaN) != 0
                        && coords.get(2).compareTo(Double.NaN) != 0) {
                        LineArray la = new LineArray(30, LineArray.COORDINATES | LineArray.COLOR_3);
                        la.setCoordinate(0, new Point3f(0f, 0, 0));
                        la.setColor(0, new Color3f(0, 0, 0));
                        la.setCoordinate(1, new Point3f((float) (coords.get(0) / max), (float) (coords.get(1) / max), (float) (coords.get(2) / max)));
                        la.setColor(1, d.getColor());
                        swbranch.addChild(new Shape3D(la));
                        Sphere sphere = new Sphere(.01f, Sphere.GENERATE_NORMALS, app);
                        Transform3D transform = new Transform3D();
                        transform.setTranslation(new Vector3f((float) (coords.get(0) / max), (float) (coords.get(1) / max), (float) (coords.get(2) / max)));
                        TransformGroup trans_group1 = new TransformGroup(transform);
                        trans_group1.addChild(sphere);
                        swbranch.addChild(trans_group1);
                        continue;
                    }
                }

                Vector3d vec = new Vector3d();
                if (coords.get(0).compareTo(Double.NaN) != 0
                    && coords.get(1).compareTo(Double.NaN) != 0
                    && coords.get(2).compareTo(Double.NaN) != 0) {
                    vec.x = (coords.get(0) / max);
                    vec.y = (coords.get(1) / max);
                    vec.z = (coords.get(2) / max);
                } else {
                    continue;
                }
                System.out.println(vec);
                Transform3D transform = new Transform3D();
                transform.setTranslation(vec);
                TransformGroup trans_group1 = new TransformGroup(transform);
                trans_group1.addChild(object);
                swbranch.addChild(trans_group1);
            }
            switchGroup.addChild(swbranch);
            address.put(s, index++);
        }
        //create the logical mask to control Node visibility
        visibleNodes = new java.util.BitSet(switchGroup.numChildren());

        //make the third, sixth and seventh nodes visible
        for (int i = 0; i < switchGroup.numChildren(); ++i) {
            visibleNodes.set(i);
        }

        //assign the visibility mask to the Switch
        switchGroup.setChildMask(visibleNodes);
        data_branch.addChild(switchGroup);
        enablePicking(data_branch);
    }

    public void mouseClicked(MouseEvent e) {
        pickCanvas.setShapeLocation(e);
        PickResult result = pickCanvas.pickClosest();
        if (result == null) {
            System.out.println("Nothing picked");
        } else {
            Primitive p = (Primitive) result.getNode(PickResult.PRIMITIVE);
            if (p != null) {
                //FIXME introduce instance content and mapping
                System.out.println(p.getClass().getName());
                info.set_coords(get_model(p));
                this.selDa = get_model(p);
            } else {
                System.out.println("Nix");
            }
        }
    }

    private DataModel get_model(Primitive p) {
        for (String s : hash.keySet()) {
            ArrayList<DataModel> list = hash.get(s);
            for (DataModel d : list) {
                Transform3D t = new Transform3D();
                p.getLocalToVworld(t);
                Vector3f coords = new Vector3f();
                t.get(coords);
				//System.out.println(coords);
                //System.out.println(d.getCoordinates().get(0)/d.getFactor());
                if (((Double) (d.getCoordinates().get(0) / d.getFactor())).floatValue() == coords.x
                    && ((Double) (d.getCoordinates().get(1) / d.getFactor())).floatValue() == coords.y
                    && ((Double) (d.getCoordinates().get(2) / d.getFactor())).floatValue() == coords.z) {
                    //System.out.println("Found one");

                    Object payload = d.getPayload();
                    if (payload != null) {
                        if (selectedPayload != null) {
                            content.remove(selectedPayload);
                        }
                        selectedPayload = payload;
                        content.add(selectedPayload);
                    }
                    return d;
                }
            }
        }
        return null;
    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void enablePicking(Node node) {
        node.setPickable(true);
        node.setCapability(Node.ENABLE_PICK_REPORTING);
        try {
            Group group = (Group) node;
            for (Enumeration e = group.getAllChildren(); e.hasMoreElements();) {
                enablePicking((Node) e.nextElement());
            }
        } catch (ClassCastException e) {
            // if not a group node, there are no children so ignore exception
        }
        try {
            Shape3D shape = (Shape3D) node;
            PickTool.setCapabilities(node, PickTool.INTERSECT_FULL);
            for (Enumeration e = shape.getAllGeometries(); e.hasMoreElements();) {
                Geometry g = (Geometry) e.nextElement();
                g.setCapability(g.ALLOW_INTERSECT);
            }
        } catch (ClassCastException e) {
            // not a Shape3D node ignore exception
        } catch (javax.media.j3d.RestrictedAccessException rae) {
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("xy")) {
            Transform3D transform = new Transform3D();
            transform.setTranslation(new Vector3f(0, 0, 3.5f));
            uni.getViewingPlatform().getViewPlatformTransform().setTransform(transform);
        } else if (e.getActionCommand().equals("xz")) {
            Transform3D transform = new Transform3D();
            transform.lookAt(new Point3d(0, 3.5, 0), new Point3d(0, 0, 0), new Vector3d(0, 0, 1));
            transform.invert();
            uni.getViewingPlatform().getViewPlatformTransform().setTransform(transform);
        } else if (e.getActionCommand().equals("yz")) {
            Transform3D transform = new Transform3D();
            transform.lookAt(new Point3d(3.5, 0, 0), new Point3d(0, 0, 0), new Vector3d(0, 0, 1));
            transform.invert();
            uni.getViewingPlatform().getViewPlatformTransform().setTransform(transform);
        } else if (e.getActionCommand().equals("change background")) {
            Color3f col = new Color3f(JColorChooser.showDialog(this,
                "Choose Background Color", Color.BLACK));
            b.setColor(col);
            if (e.getSource() instanceof JButton) {
                if (((JButton) e.getSource()).getParent() instanceof InfoPanel) {
                    ((InfoPanel) ((JButton) e.getSource()).getParent()).set_background(col);
                    ((InfoPanel) ((JButton) e.getSource()).getParent()).set_colors();
                }
            }
        } else if (e.getActionCommand().equals("export")) {
            BufferedImage bi = new java.awt.image.BufferedImage(can.getWidth(), can.getHeight(), BufferedImage.TYPE_INT_RGB);
            ImageComponent2D im2d = new ImageComponent2D(ImageComponent.FORMAT_RGB, bi);
            Raster ras = new Raster(new Point3f(-1.0f, -1.0f, -1.0f), Raster.RASTER_COLOR, 0, 0, can.getWidth(), can.getHeight(), im2d, null);
            GraphicsContext3D gc3d = can.getGraphicsContext3D();
            gc3d.readRaster(ras);
            BufferedImage bimage = ras.getImage().getImage();
            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(this);
            File f = fc.getSelectedFile();
            try {
                ImageIO.write(bimage, "png", f);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if (e.getActionCommand().equals("clip")) {
            if (selDa != null) {
                String name = selDa.getLabel();
                String anno = selDa.getAnnotation();
                Double x = selDa.getCoordinates().get(0);
                Double y = selDa.getCoordinates().get(1);
                Double z = selDa.getCoordinates().get(2);
                String con = "Name: " + name + "\nAnnotation: " + anno + "\nx: " + x
                    + "\ny: " + y + "\nz: " + z;
                StringSelection stringSelection = new StringSelection(con);
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                c.setContents(stringSelection, this);
            }
        }
    }

    public void itemStateChanged(ItemEvent evt) {
        try {
            Checkbox sender = (Checkbox) evt.getSource();
            String key = sender.getName();
            if (sender.getState()) {
                visibleNodes.set(address.get(key));
                System.out.println("setting " + key + " at " + address.get(key));
            } else {
                visibleNodes.clear(address.get(key));
                System.out.println("unsetting " + key + " at " + address.get(key));
            }
            switchGroup.setChildMask(visibleNodes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // TODO Auto-generated method stub
    }
}

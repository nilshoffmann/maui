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
package maltcms.ui.fileHandles.cdf.nodes;

import cross.datastructures.fragments.IVariableFragment;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import ucar.nc2.Attribute;

/**
 *
 * @author Nils Hoffmann
 */
public class VariableFragmentNode extends BeanNode<IVariableFragment> {
	
	public VariableFragmentNode(IVariableFragment bean) throws IntrospectionException {
		super(bean, Children.LEAF, Lookups.fixed(bean));
	}
	
	public VariableFragmentNode(IVariableFragment bean, Children children) throws IntrospectionException {
		super(bean, children);
	}
	
	public VariableFragmentNode(IVariableFragment bean, Children children, Lookup lkp) throws IntrospectionException {
		super(bean, children, lkp);
	}
	
	@Override
	protected void createProperties(IVariableFragment bean, BeanInfo info) {
		//super.createProperties(bean, info); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	protected Sheet createSheet() {
		Sheet sheet = Sheet.createDefault();
		Sheet.Set set = Sheet.createPropertiesSet();
		Property nameProp = new PropertySupport.ReadOnly<String>("name", String.class, "Name", "The name of this variable") {
			@Override
			public String getValue() throws IllegalAccessException, InvocationTargetException {
				return getBean().getName();
			}
		};
		set.put(nameProp);
		Property dataTypeProp = new PropertySupport.ReadOnly<String>("dataType", String.class, "DataType", "The data type associated with this variable") {
			@Override
			public String getValue() throws IllegalAccessException, InvocationTargetException {
				return getBean().getDataType().name();
			}
		};
		set.put(dataTypeProp);
		Property dimProp = new PropertySupport.ReadOnly<String>("dimensions", String.class, "Dimensions", "The Dimensions associated with this variable") {
			@Override
			public String getValue() throws IllegalAccessException, InvocationTargetException {
				return Arrays.deepToString(getBean().getDimensions());
			}
		};
		set.put(dimProp);
		Property rangeProp = new PropertySupport.ReadOnly<String>("ranges", String.class, "Ranges", "The ranges associated with this variable") {
			@Override
			public String getValue() throws IllegalAccessException, InvocationTargetException {
				return Arrays.deepToString(getBean().getRange());
			}
		};
		set.put(rangeProp);
		Property attributesProp = new PropertySupport.ReadOnly<String>("attributes", String.class, "Attributes", "The attributes associated with this variable") {
			@Override
			public String getValue() throws IllegalAccessException, InvocationTargetException {
				return Arrays.deepToString(getBean().getAttributes().toArray(new Attribute[getBean().getAttributes().size()]));
			}
		};
		set.put(attributesProp);
		
		sheet.put(set);
		return sheet;
		
	}
	
	@Override	
	public Action[] getActions(boolean context) {
		Action[] superActions = super.getActions(context);
		ArrayList<Action> finalActions = new ArrayList<Action>();
		List<? extends Action> actions = Utilities.actionsForPath("Actions/VariableFragmentActions");
		finalActions.addAll(actions);
		finalActions.add(null);
		finalActions.addAll(Arrays.asList(superActions));
		return finalActions.toArray(new Action[finalActions.size()]);
	}
}

package net.lecousin.framework.ui.eclipse.control;

import java.util.LinkedList;
import java.util.List;

import net.lecousin.framework.Pair;
import net.lecousin.framework.event.Event;
import net.lecousin.framework.thread.RunnableWithData;
import net.lecousin.framework.ui.eclipse.UIUtil;
import net.lecousin.framework.ui.eclipse.control.button.ButtonStyle;
import net.lecousin.framework.ui.eclipse.control.button.ButtonStyleApply;
import net.lecousin.framework.ui.eclipse.dialog.FlatPopupMenu;
import net.lecousin.framework.ui.eclipse.graphics.ColorUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class LCCombo extends Composite {

	/** image and/or text may be null */
	public LCCombo(Composite parent, Image image) {
		super(parent, SWT.NONE);
		setBackground(parent.getBackground());
		GridLayout layout = UIUtil.gridLayout(this, 2, 0, 0, 1, 0);
		if (image != null) layout.numColumns++;
		setLayout(layout);
		
		if (image != null)
			UIUtil.newImage(this, image);
		text = UIUtil.newText(this, "", null);
		text.setLayoutData(UIUtil.gridDataHoriz(1, true));
		text.addModifyListener(new TextListener());
		Canvas canvas = new Canvas(this, SWT.NONE);
		canvas.setBackground(getBackground());
		GridData gd = new GridData();
		gd.widthHint = 9;
		gd.heightHint = 6;
		gd.verticalAlignment = SWT.FILL;
		canvas.setLayoutData(gd);
		canvas.addPaintListener(new ArrowPainter());
		
		ButtonStyle style = new ButtonStyle();
		new ButtonStyleApply(canvas, style);
		canvas.addMouseListener(new ButtonMouseListener());
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				text = null;
				items.clear(); items = null;
				selection = null;
				selectionEvent.free(); selectionEvent = null;
			}
		});
	}
	
	private Text text;
	private List<Item> items = new LinkedList<Item>();
	private Item selection = null;
	private Event<Pair<String,Object>> selectionEvent = new Event<Pair<String,Object>>();
	
	private class Item {
		Item(Image i, String t, Object d)
		{ image = i; text = t; data = d; }
		Image image;
		String text;
		Object data;
	}
	
	public void setEditable(boolean value) {
		text.setEditable(value);
	}
	public void addItem(Image image, String text, Object data) {
		items.add(new Item(image, text, data));
	}
	public void clear() {
		items.clear();
	}
	
	public String getSelection() { return text.getText(); }
	public Object getSelectionData() { return selection != null ? selection.data : null; }
	
	public Event<Pair<String,Object>> selectionEvent() { return selectionEvent; }
	
	private void fireSelection() {
		if (selection == null && text.getText().length() == 0)
			selectionEvent.fire(null);
		else
			selectionEvent.fire(new Pair<String,Object>(text.getText(), selection != null ? selection.data : null));
	}
	
	public void setSelection(String text) {
		if (!this.text.getText().equals(text))
			this.text.setText(text);
		for (Item i : items)
			if (i.text.equals(text)) {
				selection = i;
			}
		fireSelection();
	}
	
	private void showMenu() {
		FlatPopupMenu dlg = new FlatPopupMenu(this, null, false, true, false, true);
		for (Item i : items) {
			new FlatPopupMenu.Menu(dlg, i.text, i.image, false, false, new RunnableWithData<Item>(i) {
				public void run() {
					selection = data();
					text.setText(data().text);
					fireSelection();
				}
			});
		}
		dlg.setMinWidth(getSize().x);
		dlg.show(this, FlatPopupMenu.Orientation.BOTTOM, true);
	}
	
	private static class ArrowPainter implements PaintListener {
		public void paintControl(PaintEvent e) {
			Point size = ((Canvas)e.widget).getSize();
			e.gc.setForeground(ColorUtil.get(0, 0, 60));
			int y = size.y/2-1;
			e.gc.drawLine(1, y, 7, y);
			e.gc.drawLine(2, y+1, 6, y+1);
			e.gc.drawLine(3, y+2, 5, y+2);
			e.gc.drawPoint(4, y+3);
		}
	}
	
	private class ButtonMouseListener implements MouseListener {
		ButtonMouseListener() { }
		public void mouseDoubleClick(MouseEvent e) {
		}
		public void mouseDown(MouseEvent e) {
		}
		public void mouseUp(MouseEvent e) {
			if (e.button == 1) {
				showMenu();
			}
		}
	}
	
	private class TextListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			setSelection(text.getText());
		}
	}
}

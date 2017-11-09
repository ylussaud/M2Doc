/*******************************************************************************
 *  Copyright (c) 2017 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/
package org.obeonetwork.m2doc.tests.services;

import java.awt.Color;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.obeonetwork.m2doc.element.MBookmark;
import org.obeonetwork.m2doc.element.MElement;
import org.obeonetwork.m2doc.element.MImage;
import org.obeonetwork.m2doc.element.MList;
import org.obeonetwork.m2doc.element.MPagination;
import org.obeonetwork.m2doc.element.MStyle;
import org.obeonetwork.m2doc.element.MTable;
import org.obeonetwork.m2doc.element.MText;
import org.obeonetwork.m2doc.element.impl.MBookmarkImpl;
import org.obeonetwork.m2doc.element.impl.MImageImpl;
import org.obeonetwork.m2doc.element.impl.MListImpl;
import org.obeonetwork.m2doc.element.impl.MStyleImpl;
import org.obeonetwork.m2doc.element.impl.MTextImpl;

/**
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class MListTestServices {

    /**
     * {@link List} of sample {@link MElement}.
     */
    private final static List<MElement> SAMPLE_MELEMENTS = initSampleMElements();

    private final static MStyle HEADER_STYLE = new MStyleImpl(14, Color.GRAY, MStyle.FONT_BOLD | MStyle.FONT_UNDERLINE);

    /**
     * @return
     */
    private static MList initSampleMElements() {
        final MList res = new MListImpl();

        final MBookmark bookmark = new MBookmarkImpl("bookmark", "id", false);
        res.add(bookmark);

        final MBookmark bookmarkRef = new MBookmarkImpl("bookmarkRef", "id", true);
        res.add(bookmarkRef);

        final MImage image = new MImageImpl(URI.createURI("resources/query/sampleMList/dh1.gif"));
        image.setHeight(200);
        res.add(image);

        final MTable table = new MTableTestServices().sampleTable("sample table");
        res.add(table);

        final MText text = new MTextImpl("some text",
                new MStyleImpl(6, Color.ORANGE, MStyle.FONT_BOLD | MStyle.FONT_ITALIC));
        res.add(text);

        final MList list = new MListImpl();
        final MText textInList = new MTextImpl("text in a list", new MStyleImpl(6, Color.GREEN, MStyle.FONT_BOLD));
        list.add(textInList);
        res.add(list);

        return res;
    }

    public MList emptyList(Object object) {
        return new MListImpl();
    }

    public MList sampleList(Object object) {
        final MList res = new MListImpl();

        int index = 0;
        // for (MElement before : SAMPLE_MELEMENTS) {
        // for (MElement current : SAMPLE_MELEMENTS) {
        for (MElement after : SAMPLE_MELEMENTS) {
            // final MText header = createHeader(before, current, after);
            // res.add(header);
            // res.add(getElementToInsert(before, index));
            // if (before instanceof MBookmark && !((MBookmark) before).isReference()) {
            // index++;
            // }
            // res.add(getElementToInsert(current, index));
            // if (current instanceof MBookmark && !((MBookmark) current).isReference()) {
            // index++;
            // }
            res.add(getElementToInsert(after, index));
            if (after instanceof MBookmark && !((MBookmark) after).isReference()) {
                index++;
            }
            res.add(new MTextImpl("\n", null));
        }
        // res.add(new MTextImpl("\n", null));
        // }
        // res.add(new MTextImpl("\n", null));
        // }

        return res;
    }

    /**
     * Creates the {@link MText header} for the given {@link MElement}.
     * 
     * @param before
     *            the first {@link MElement}
     * @param current
     *            the second {@link MElement}
     * @param after
     *            the last {@link MElement}
     * @return the {@link MText header} for the given {@link MElement}
     */
    private MText createHeader(MElement before, MElement current, MElement after) {
        final String text = getText(before) + " - " + getText(current) + " - " + getText(after) + '\n';

        return new MTextImpl(text, HEADER_STYLE);
    }

    /**
     * Gets the text to display in header for the given {@link MElement}.
     * 
     * @param element
     *            the {@link MElement}
     * @return
     */
    private String getText(MElement element) {
        final String res;

        if (element instanceof MPagination) {
            res = ((MPagination) element).name();
        } else {
            res = element.getClass().getSimpleName();
        }

        return res;
    }

    /**
     * Creates a clone with given id for {@link MBookmark}.
     * 
     * @param element
     *            the {@link MElement} to insert
     * @param id
     *            the id suffix for the new {@link MBookmark}
     * @return
     */
    private MElement getElementToInsert(MElement element, int id) {
        final MElement res;

        if (element instanceof MBookmark) {
            final MBookmark bookmark = (MBookmark) element;
            res = new MBookmarkImpl(bookmark.getText() + id, bookmark.getId() + id, bookmark.isReference());
        } else {
            res = element;
        }

        return res;
    }

}

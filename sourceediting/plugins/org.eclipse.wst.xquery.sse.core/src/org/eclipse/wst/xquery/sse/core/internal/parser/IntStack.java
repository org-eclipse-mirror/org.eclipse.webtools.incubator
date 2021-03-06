/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *******************************************************************************/
package org.eclipse.wst.xquery.sse.core.internal.parser;

/**
 * A resizable class implementing the behavior of java.util.Stack, but
 * directly for the <code> integer </code> primitive.
 */
import java.util.EmptyStackException;

public class IntStack {
	private int[] list = null;

	private int size = 0;

	public IntStack() {
		this(100);
	}

	public IntStack(int maxdepth) {
		super();
		list = new int[maxdepth];
		initialize();
	}

	public boolean empty() {
		return size == 0;
	}

	public int get(int slot) {
		return list[slot];
	}

	void initialize() {

		for (int i = 0; i < list.length; i++)
			list[i] = -1;
	}

	/**
	 * Returns the int at the top of the stack without removing it
	 * 
	 * @return int at the top of this stack.
	 * @exception EmptyStackException
	 *                when empty.
	 */
	public int peek() {
		if (size == 0)
			throw new EmptyStackException();
		return list[size - 1];
	}

	/**
	 * Removes and returns the int at the top of the stack
	 * 
	 * @return int at the top of this stack.
	 * @exception EmptyStackException
	 *                when empty.
	 */
	public int pop() {
		int value = peek();
		list[size - 1] = -1;
		size--;
		return value;
	}

	/**
	 * Pushes an item onto the top of this stack.
	 * 
	 * @param newValue
	 *            - the int to be pushed onto this stack.
	 * @return the <code>newValue</code> argument.
	 */
	public int push(int newValue) {
		if (size == list.length) {
			int[] newlist = new int[list.length + (list.length * 1 / 3)];
			System.arraycopy(list, 0, newlist, 0, list.length);
			list = newlist;
		}
		list[size++] = newValue;
		return newValue;
	}

	/** Set value on top of the stack */
	public void setTop(int newValue) {
		list[size - 1] = newValue;
	}

	public void reset() {
		size = 0;
	}
}

/*   This file is part of lanSimulation.
 *
 *   lanSimulation is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   lanSimulation is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with lanSimulation; if not, write to the Free Software
 *   Foundation, Inc. 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *   Copyright original Java version: 2004 Bart Du Bois, Serge Demeyer
 *   Copyright C++ version: 2006 Matthias Rieger, Bart Van Rompaey
 */
package lanSimulation.internals;

import java.io.IOException;
import java.io.Writer;

import lanSimulation.Network;

/**
 * A <em>Node</em> represents a single Node in a Local Area Network (LAN).
 * Several types of Nodes exist.
 */
public class Node {
	/**
	 * Holds the type of the Node.
	 */
	private byte type_;

	/**
	 * Holds the name of the Node.
	 */
	public String name_;
	/**
	 * Holds the next Node in the token ring architecture.
	 * 
	 * @see lanSimulation.internals.Node
	 */
	public Node nextNode_;

	/**
	 * Construct a <em>Node</em> with given #type and #name.
	 * <p>
	 * <strong>Precondition:</strong> (type >= NODE) & (type <= PRINTER);
	 * </p>
	 */
	public Node(String name) {
		//assert (type >= NodeType.NODE) & (type <= NodeType.PRINTER);
		//setType_(type);
		name_ = name;
		nextNode_ = null;
	}

	/**
	 * Construct a <em>Node</em> with given #type and #name, and which is linked to
	 * #nextNode.
	 * <p>
	 * <strong>Precondition:</strong> (type >= NODE) & (type <= PRINTER);
	 * </p>
	 */
	public Node(String name, Node nextNode) {
		//assert (type >= NodeType.NODE) & (type <= NodeType.PRINTER);
		//setType_(type);
		name_ = name;
		nextNode_ = nextNode;
	}

	public void logging(Writer report, String mensaje) throws IOException {
		report.write("\tNode '");
		report.write(name_);
		report.write("' " + mensaje + "\n");
		report.flush();
	}

	public boolean printDocument(Network network, Packet document, Writer report) {
		String author = "Unknown";
		String title = "Untitled";
		int startPos = 0, endPos = 0;

		if (this instanceof Printer) {
			try {
				String tipo;
				if (document.message_.startsWith("!PS")) {
					startPos = document.message_.indexOf("author:");
					if (startPos >= 0) {
						endPos = document.message_.indexOf(".", startPos + 7);
						if (endPos < 0) {
							endPos = document.message_.length();
						}

						author = document.message_.substring(startPos + 7, endPos);
					}
					startPos = document.message_.indexOf("title:");
					if (startPos >= 0) {
						endPos = document.message_.indexOf(".", startPos + 6);
						if (endPos < 0) {
							endPos = document.message_.length();
						}
						title = document.message_.substring(startPos + 6, endPos);
					}
					tipo = ">>> Postscript job delivered.\n\n";
					network.printAuthor(report, author, title, tipo);
				} else {
					title = "ASCII DOCUMENT";
					if (document.message_.length() >= 16) {
						author = document.message_.substring(8, 16);
					}
					tipo = ">>> ASCII Print job delivered.\n\n";
					network.printAuthor(report, author, title, tipo);
				}

			} catch (IOException exc) {
				// just ignore
			}

			return true;
		} else {
			try {
				report.write(">>> Destinition is not a printer, print job cancelled.\n\n");
				report.flush();
			} catch (IOException exc) {
				// just ignore
			}
			return false;
		}
	}

	/**
	 * Write a printable representation of #receiver on the given #buf.
	 * <p>
	 * <strong>Precondition:</strong> isInitialized();
	 * </p>
	 * 
	 * @param network
	 *            TODO
	 * @param buf
	 *            TODO
	 */
	public void printOnGeneral(Network network, StringBuffer buf) {
		assert network.isInitialized();
		Node currentNode = this;
		do {
			currentNode.printOn(currentNode, buf);
			buf.append(" -> ");
			currentNode = currentNode.nextNode_;
		} while (currentNode != this);
		buf.append(" ... ");
	}

	public void printOn(Node node,StringBuffer buf) {
		printHTMLOn(buf, node);
	}
	
	/**
	 * Write a HTML representation of #receiver on the given #buf.
	 * <p>
	 * <strong>Precondition:</strong> isInitialized();
	 * </p>
	 * 
	 * @param network
	 *            TODO
	 * @param buf
	 *            TODO
	 */
	public void printHTMLOnGeneral(Network network, StringBuffer buf) {
		assert network.isInitialized();

		buf.append("<HTML>\n<HEAD>\n<TITLE>LAN Simulation</TITLE>\n</HEAD>\n<BODY>\n<H1>LAN SIMULATION</H1>");
		Node currentNode = this;
		buf.append("\n\n<UL>");
		do {
			buf.append("\n\t<LI> ");
			currentNode.printHTMLOn(buf, currentNode);
			buf.append(" </LI>");
			currentNode = currentNode.nextNode_;
		} while (currentNode != this);
		buf.append("\n\t<LI>...</LI>\n</UL>\n\n</BODY>\n</HTML>\n");
	}

	public void printHTMLOn(StringBuffer buf, Node currentNode) {
		buf.append("Node ");
		buf.append(currentNode.name_);
		buf.append(" [Node]");
	}

	/**
	 * Write an XML representation of #receiver on the given #buf.
	 * <p>
	 * <strong>Precondition:</strong> isInitialized();
	 * </p>
	 * 
	 * @param network
	 *            TODO
	 * @param buf
	 *            TODO
	 */
	public void printXMLOnGeneral(Network network, StringBuffer buf) {
		assert network.isInitialized();
		Node currentNode = this;
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<network>");
		do {
			buf.append("\n\t");
			currentNode.printXMLOn(buf, currentNode);
			currentNode = currentNode.nextNode_;
		} while (currentNode != this);
		buf.append("\n</network>");
	}

	public void printXMLOn(StringBuffer buf, Node currentNode) {
		buf.append("<node>");
		buf.append(currentNode.name_);
		buf.append("</node>");
	}

	public byte getType_() {
		return type_;
	}

	public void setType_(byte type_) {
		this.type_ = type_;
	}

}
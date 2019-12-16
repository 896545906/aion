package org.aion.zero.impl.config;

import com.google.common.base.Objects;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/** @author chris */
public final class CfgSync {

    private boolean showStatus;
    private Set<StatsType> showStatistics;

    public CfgSync() {
        this.showStatus = false;
        this.showStatistics = new HashSet<>();
        this.showStatistics.add(StatsType.NONE);
    }

    public void fromXML(final XMLStreamReader sr) throws XMLStreamException {
        loop:
        while (sr.hasNext()) {
            int eventType = sr.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = sr.getLocalName().toLowerCase();
                    switch (elementName) {
                        case "show-status":
                            this.showStatus = Boolean.parseBoolean(ConfigUtil.readValue(sr));
                            break;
                        case "show-statistics":
                            parseSelectedStats(showStatistics, ConfigUtil.readValue(sr));
                            break;
                        default:
                            ConfigUtil.skipElement(sr);
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break loop;
            }
        }
    }

    private static void parseSelectedStats(Set<StatsType> showStatistics, String readValue) {
        showStatistics.clear();

        String[] selected = readValue.split(",");

        for (String option : selected) {
            try {
                showStatistics.add(StatsType.valueOf(option.toUpperCase()));
            } catch (RuntimeException e) {
                // skip option
            }
        }

        // expand all to specific options
        if (showStatistics.contains(StatsType.ALL)) {
            showStatistics.remove(StatsType.ALL);
            showStatistics.addAll(StatsType.getAllSpecificTypes());
        }

        if (showStatistics.contains(StatsType.NONE) && showStatistics.size() > 1) {
            showStatistics.remove(StatsType.NONE);
        }

        // set none if empty
        if (showStatistics.isEmpty()) {
            showStatistics.add(StatsType.NONE);
        }
    }

    public String toXML() {
        final XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter;
        String xml;
        try {
            Writer strWriter = new StringWriter();
            xmlWriter = output.createXMLStreamWriter(strWriter);

            // start element sync
            xmlWriter.writeCharacters("\r\n\t");
            xmlWriter.writeStartElement("sync");

            // sub-element show-status
            xmlWriter.writeCharacters("\r\n\t\t");
            xmlWriter.writeStartElement("show-status");
            xmlWriter.writeCharacters(this.showStatus + "");
            xmlWriter.writeEndElement();

            // sub-element show-status
            xmlWriter.writeCharacters("\r\n\t\t");
            xmlWriter.writeComment(
                    "requires show-status=true; comma separated list of options: "
                            + Arrays.toString(StatsType.values()).toLowerCase());
            xmlWriter.writeCharacters("\r\n\t\t");
            xmlWriter.writeStartElement("show-statistics");
            xmlWriter.writeCharacters(printSelectedStats().toLowerCase());
            xmlWriter.writeEndElement();

            // close element sync
            xmlWriter.writeCharacters("\r\n\t");
            xmlWriter.writeEndElement();

            xml = strWriter.toString();
            strWriter.flush();
            strWriter.close();
            xmlWriter.flush();
            xmlWriter.close();
            return xml;
        } catch (IOException | XMLStreamException e) {
            return "";
        }
    }

    /**
     * Returns a string containing a comma separated list of the statistics to be displayed.
     *
     * @return a string containing a comma separated list of the statistics to be displayed
     */
    private String printSelectedStats() {
        // not meaningful if other settings are also present
        showStatistics.remove(StatsType.NONE);

        if (showStatistics.isEmpty()) {
            return StatsType.NONE.toString();
        } else {
            if (showStatistics.contains(StatsType.ALL)
                    || showStatistics.containsAll(StatsType.getAllSpecificTypes())) {
                return StatsType.ALL.toString();
            } else if (showStatistics.size() == 1) {
                return showStatistics.iterator().next().toString();
            } else {
                StatsType first = showStatistics.iterator().next();
                showStatistics.remove(first);
                StringBuilder sb = new StringBuilder(first.toString());
                for (StatsType tp : showStatistics) {
                    sb.append(",");
                    sb.append(tp.toString());
                }
                return sb.toString();
            }
        }
    }

    public void setShowStatus(boolean showStatus) {
        this.showStatus = showStatus;
    }

    public boolean getShowStatus() {
        return this.showStatus;
    }

    public Set<StatsType> getShowStatistics() {
        return showStatistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgSync cfgSync = (CfgSync) o;
        return showStatus == cfgSync.showStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(showStatus);
    }
}

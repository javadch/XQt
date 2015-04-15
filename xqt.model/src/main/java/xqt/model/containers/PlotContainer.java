/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.containers;

import java.util.ArrayList;
import java.util.List;
import xqt.model.declarations.PerspectiveDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class PlotContainer extends DataContainer {
    private PerspectiveDescriptor linkedPerspective;
    private String hax;
    private List<String> vaxes = new ArrayList<>();
    private String plotType;
    private String hLabel;
    private String vLabel;
    private String plotLabel;

    public PlotContainer(){
        this.dataContainerType = DataContainerType.Plot;
    }
    
    public PerspectiveDescriptor getLinkedPerspective() {
        return linkedPerspective;
    }

    public void setLinkedPerspective(PerspectiveDescriptor linkedPerspective) {
        this.linkedPerspective = linkedPerspective;
    }

    public String getPlotName() {
        return id;
    }

    public void setPlotName(String plotName) {
        this.id = plotName;
    }

    public String getHax() {
        return hax;
    }

    public void setHax(String hax) {
        this.hax = hax;
    }

    public List<String> getVaxes() {
        return vaxes;
    }

    public void setVax(List<String> vaxes) {
        this.vaxes = vaxes;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public String gethLabel() {
        return hLabel;
    }

    public void sethLabel(String hLabel) {
        this.hLabel = hLabel;
    }

    public String getvLabel() {
        return vLabel;
    }

    public void setvLabel(String vLabel) {
        this.vLabel = vLabel;
    }

    public String getPlotLabel() {
        return plotLabel;
    }

    public void setPlotLabel(String plotLabel) {
        this.plotLabel = plotLabel;
    }           
}
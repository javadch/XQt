/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.containers;

import xqt.model.BaseDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class DataContainer extends BaseDescriptor{
    public enum DataContainerType {
        Single, Joined, Variable, Plot
    }
    
    public enum ContainerLifeTime{
        Long, Short
    }

    protected DataContainer.ContainerLifeTime lifeTime = DataContainer.ContainerLifeTime.Long;
    protected DataContainer.DataContainerType dataContainerType = DataContainer.DataContainerType.Single;

    public DataContainer.ContainerLifeTime getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(DataContainer.ContainerLifeTime lifeTime) {
        this.lifeTime = lifeTime;
    }
    
    public DataContainer.DataContainerType getDataContainerType() {
        return dataContainerType;
    }

    public void setDataContainerType(DataContainer.DataContainerType dataContainerType) {
        this.dataContainerType = dataContainerType;
    }

}

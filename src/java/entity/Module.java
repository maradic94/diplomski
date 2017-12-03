/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries(
        value = {
            @NamedQuery(name = "allModules", query = "from Module"),
            @NamedQuery(name = "getModuleByName", query = "from Module where moduleName = :modulename")
        }
)
public class Module implements Serializable {

    @Id
    @GeneratedValue
    private int moduleID;

    public Module() {
    }

    @Column(nullable = false, unique = true, name = "module_name")
    private String moduleName;

    @Column(nullable = false, name = "allow_change")
    private boolean allowChange;

    public Module(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public boolean isAllowChange() {
        return allowChange;
    }

    public void setAllowChange(boolean allowChange) {
        this.allowChange = allowChange;
    }

}

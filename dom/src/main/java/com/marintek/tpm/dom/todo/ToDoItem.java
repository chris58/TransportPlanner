/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package com.marintek.tpm.dom.todo;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.jdo.spi.PersistenceCapable;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberGroups;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Resolve;
import org.apache.isis.applib.annotation.Resolve.Type;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.filter.Filters;
import org.apache.isis.applib.util.TitleBuffer;
import org.apache.isis.core.objectstore.jdo.applib.annotations.Auditable;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries( {
    @javax.jdo.annotations.Query(
            name="todo_all", language="JDOQL",  
            value="SELECT FROM dom.todo.ToDoItem WHERE ownedBy == :ownedBy"),
    @javax.jdo.annotations.Query(
        name="todo_notYetComplete", language="JDOQL",  
        value="SELECT FROM dom.todo.ToDoItem WHERE ownedBy == :ownedBy && complete == false"),
    @javax.jdo.annotations.Query(
            name="todo_complete", language="JDOQL",  
            value="SELECT FROM dom.todo.ToDoItem WHERE ownedBy == :ownedBy && complete == true"),
    @javax.jdo.annotations.Query(
        name="todo_similarTo", language="JDOQL",  
        value="SELECT FROM dom.todo.ToDoItem WHERE ownedBy == :ownedBy && category == :category"),
    @javax.jdo.annotations.Query(
            name="todo_autoComplete", language="JDOQL",  
            value="SELECT FROM dom.todo.ToDoItem WHERE ownedBy == :ownedBy && description.startsWith(:description)")
})
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("TODO")
@Auditable
@AutoComplete(repository=ToDoItems.class, action="autoComplete")
@MemberGroups({"General", "Detail"})
public class ToDoItem implements Comparable<ToDoItem> {

	private static final long ONE_WEEK_IN_MILLIS = 7 * 24 * 60 * 60 * 1000L;

    public static enum Category {
        Professional, Domestic, Other;
    }

    // {{ Identification on the UI
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append(getDescription());
        if (isComplete()) {
            buf.append(" - Completed!");
        } else {
            if (getDueBy() != null) {
                buf.append(" due by ", getDueBy());
            }
        }
        return buf.toString();
    }

    // }}

    // {{ Description
    private String description;

    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
    // words, spaces and selected punctuation
    @MemberOrder(sequence = "1")
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
    // }}

    // {{ DueBy (property)
    private LocalDate dueBy;

    @javax.jdo.annotations.Persistent
    @MemberOrder(name="Detail", sequence = "3")
    @Optional
    public LocalDate getDueBy() {
        return dueBy;
    }

    public void setDueBy(final LocalDate dueBy) {
        this.dueBy = dueBy;
    }
    // proposed new value is validated before setting
    public String validateDueBy(final LocalDate dueBy) {
        if (dueBy == null) {
            return null;
        }
        return isMoreThanOneWeekInPast(dueBy) ? "Due by date cannot be more than one week old" : null;
    }
    // }}

    // {{ Category
    private Category category;

    @MemberOrder(sequence = "2")
    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }
    // }}

    // {{ OwnedBy (property)
    private String ownedBy;

    @Hidden
    // not shown in the UI
    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(final String ownedBy) {
        this.ownedBy = ownedBy;
    }

    // }}

    // {{ Complete (property)
    private boolean complete;

    @Disabled
    // cannot be edited as a property
    @MemberOrder(sequence = "4")
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(final boolean complete) {
        this.complete = complete;
    }

    // {{ Notes (property)
    private String notes;

    @Hidden(where=Where.ALL_TABLES)
    @Optional
    @MultiLine(numberOfLines=5)
    @MemberOrder(name="Detail", sequence = "6")
    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }
    // }}


    // {{ Version (derived property)
    @Hidden(where=Where.ALL_TABLES)
    @Disabled
    @MemberOrder(name="Detail", sequence = "99")
    @Named("Version")
    public Long getVersionSequence() {
        if(!(this instanceof PersistenceCapable)) {
            return null;
        } 
        PersistenceCapable persistenceCapable = (PersistenceCapable) this;
        final Long version = (Long) JDOHelper.getVersion(persistenceCapable);
        return version;
    }
    public boolean hideVersionSequence() {
        return !(this instanceof PersistenceCapable);
    }
    // }}

    // {{ completed (action)
    @MemberOrder(sequence = "1")
    public ToDoItem completed() {
        setComplete(true);
        return this;
    }

    // disable action dependent on state of object
    public String disableCompleted() {
        return complete ? "Already completed" : null;
    }

    // }}

    // {{ notYetCompleted (action)
    @MemberOrder(sequence = "2")
    public ToDoItem notYetCompleted() {
        setComplete(false);
        return this;
    }


    // disable action dependent on state of object
    public String disableNotYetCompleted() {
        return !complete ? "Not yet completed" : null;
    }
    // }}
    
    // {{ dependencies (Collection)
    private List<ToDoItem> dependencies = new ArrayList<ToDoItem>();

    @Disabled
    @MemberOrder(sequence = "1")
    @Resolve(Type.EAGERLY)
    public List<ToDoItem> getDependencies() {
        return dependencies;
    }

    public void setDependencies(final List<ToDoItem> dependencies) {
        this.dependencies = dependencies;
    }
    // }}

    @MemberOrder(name="dependencies", sequence = "99")
    public ToDoItem addNew(
            @Named("Description") String description, 
            @Named("Category") Category category,
            @Named("Due by") LocalDate dueBy) {
        final String ownedBy = toDoItems.currentUserName();
        ToDoItem toDoItem = toDoItems.newToDo(description, category, ownedBy, dueBy);
        getDependencies().add(toDoItem);
        return this;
    }
    
    
    // {{ add (action)
    @MemberOrder(name="dependencies", sequence = "3")
    public ToDoItem add(final ToDoItem toDoItem) {
        getDependencies().add(toDoItem);
        return this;
    }
    public String validateAdd(final ToDoItem toDoItem) {
        if(getDependencies().contains(toDoItem)) {
            return "Already a dependency";
        }
        if(toDoItem == this) {
            return "Can't set up a dependency to self";
        }
        return null;
    }
    public List<ToDoItem> choices0Add(/*Category cat*/) {
        return toDoItems.inCategory(Category.Domestic);
    }
    // }}

    // {{ remove (action)
    @MemberOrder(name="dependencies", sequence = "4")
    public ToDoItem remove(final ToDoItem toDoItem) {
        getDependencies().remove(toDoItem);
        return this;
    }
    public String disableRemove(final ToDoItem toDoItem) {
        return getDependencies().isEmpty()? "No dependencies to remove": null;
    }
    public String validateRemove(final ToDoItem toDoItem) {
        if(!getDependencies().contains(toDoItem)) {
            return "Not a dependency";
        }
        return null;
    }
    public List<ToDoItem> choices0Remove() {
        return getDependencies();
    }
    // }}


    // {{ clone (action)
    @Named("Clone")
    // the name of the action in the UI
    @MemberOrder(sequence = "3")
    // nb: method is not called "clone()" is inherited by java.lang.Object and
    // (a) has different semantics and (b) is in any case automatically ignored
    // by the framework
    public ToDoItem duplicate() {
        return toDoItems.newToDo(getDescription() + " - Copy", getCategory(), getDueBy());
    }
    // }}

    // {{ isDue (programmatic)
    @Programmatic
    // excluded from the framework's metamodel
    public boolean isDue() {
        if (getDueBy() == null) {
            return false;
        }
        return !isMoreThanOneWeekInPast(getDueBy());
    }

    // }}


    // {{ SimilarItems (derived collection)
    @MemberOrder(sequence = "5")
    @NotPersisted
    @Resolve(Type.EAGERLY)
    public List<ToDoItem> getSimilarItems() {
        return toDoItems.similarTo(this);
    }

    // }}



    // {{ compareTo (programmatic)
    /**
     * by complete flag, then due by date, then description
     */
    @Programmatic
    // exclude from the framework's metamodel
    @Override
    public int compareTo(final ToDoItem other) {
        if (isComplete() && !other.isComplete()) {
            return +1;
        }
        if (!isComplete() && other.isComplete()) {
            return -1;
        }
        if (getDueBy() == null && other.getDueBy() != null) {
            return +1;
        }
        if (getDueBy() != null && other.getDueBy() == null) {
            return -1;
        }
        if (getDueBy() == null && other.getDueBy() == null || getDueBy().equals(this.getDueBy())) {
            return getDescription().compareTo(other.getDescription());
        }
        return getDueBy().compareTo(getDueBy());
    }

    // }}

    // {{ helpers
    private static boolean isMoreThanOneWeekInPast(final LocalDate dueBy) {
        return dueBy.toDateTimeAtStartOfDay().getMillis() < Clock.getTime() - ONE_WEEK_IN_MILLIS;
    }

    // }}

    // {{ filters (programmatic)
    @SuppressWarnings("unchecked")
    public static Filter<ToDoItem> thoseDue() {
        return Filters.and(Filters.not(thoseComplete()), new Filter<ToDoItem>() {
            @Override
            public boolean accept(final ToDoItem t) {
                return t.isDue();
            }
        });
    }

    public static Filter<ToDoItem> thoseComplete() {
        return new Filter<ToDoItem>() {
            @Override
            public boolean accept(final ToDoItem t) {
                return t.isComplete();
            }
        };
    }

    public static Filter<ToDoItem> thoseOwnedBy(final String currentUser) {
        return new Filter<ToDoItem>() {
            @Override
            public boolean accept(final ToDoItem toDoItem) {
                return Objects.equal(toDoItem.getOwnedBy(), currentUser);
            }

        };
    }

    // chris
    public static Filter<ToDoItem> thoseInCategory(final Category category) {
        return new Filter<ToDoItem>() {
            @Override
            public boolean accept(final ToDoItem toDoItem) {
                return Objects.equal(toDoItem.getCategory(), category);
            }
        };
    }

    
    public static Filter<ToDoItem> thoseSimilarTo(final ToDoItem toDoItem) {
        return new Filter<ToDoItem>() {
            @Override
            public boolean accept(final ToDoItem eachToDoItem) {
                return Objects.equal(toDoItem.getCategory(), eachToDoItem.getCategory()) && 
                       Objects.equal(toDoItem.getOwnedBy(), eachToDoItem.getOwnedBy()) &&
                       eachToDoItem != toDoItem;
            }

        };
    }


    // }}

    // {{ injected: DomainObjectContainer
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
    // }}

    // {{ injected: ToDoItems
    private ToDoItems toDoItems;

    public void setToDoItems(final ToDoItems toDoItems) {
        this.toDoItems = toDoItems;
    }
    // }}


    
}


package org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.service.membership;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.factory.qualipso.service.wsclientmember.net.java.dev.jaxb.array.StringArray;
import org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.group.Group;
import org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.profile.Profile;
import org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.profile_info.ProfileInfo;
import org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.profile_info.ProfileInfoArray;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "membership", targetNamespace = "http://org.qualipso.factory.ws/service/membership")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
	org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.service.membership.ObjectFactory.class,
	org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.group.ObjectFactory.class,
	org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.link.ObjectFactory.class,
	org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.profile_info.ObjectFactory.class,
	org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.profile.ObjectFactory.class,
	org.factory.qualipso.service.wsclientmember.net.java.dev.jaxb.array.ObjectFactory.class,
    org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.folder.ObjectFactory.class,
    org.factory.qualipso.service.wsclientmember.ws.factory.qualipso.org.resource.file.ObjectFactory.class
})
public interface Membership {


    /**
     * 
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void addMemberInGroup(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void createGroup(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1,
        @WebParam(name = "arg2", partName = "arg2")
        String arg2)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void createProfile(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1,
        @WebParam(name = "arg2", partName = "arg2")
        String arg2,
        @WebParam(name = "arg3", partName = "arg3")
        int arg3)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void deleteGroup(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void deleteProfile(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns ws.factory.qualipso.org.service.membership.FactoryResource
     * @throws FactoryException_Exception
     */
    @WebMethod
    @WebResult(partName = "return")
    public FactoryResource findResource(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws FactoryException_Exception
    ;

    /**
     * 
     * @return
     *     returns net.java.dev.jaxb.array.StringArray
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "subjects", partName = "subjects")
    public StringArray getConnectedIdentifierSubjects()
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns ws.factory.qualipso.org.resource.profile_info.ProfileInfo
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "profile-info", partName = "profile-info")
    public ProfileInfo getProfileInfo(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "connected-profile-path", partName = "connected-profile-path")
    public String getProfilePathForConnectedIdentifier()
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "profile-path", partName = "profile-path")
    public String getProfilePathForIdentifier(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "profiles-path", partName = "profiles-path")
    public String getProfilesPath()
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @return
     *     returns net.java.dev.jaxb.array.StringArray
     */
    @WebMethod
    @WebResult(partName = "return")
    public StringArray getResourceTypeList();

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String getServiceName();

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns boolean
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(partName = "return")
    public boolean isMember(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns net.java.dev.jaxb.array.StringArray
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "groups-list", partName = "groups-list")
    public StringArray listGroups(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns net.java.dev.jaxb.array.StringArray
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "members-list", partName = "members-list")
    public StringArray listMembers(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns ws.factory.qualipso.org.resource.profile_info.ProfileInfoArray
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "profile-infos", partName = "profile-infos")
    public ProfileInfoArray listProfileInfos(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns ws.factory.qualipso.org.resource.group.Group
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "group", partName = "group")
    public Group readGroup(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns ws.factory.qualipso.org.resource.profile.Profile
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    @WebResult(name = "profile", partName = "profile")
    public Profile readProfile(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void removeMemberFromGroup(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void setProfileInfo(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1,
        @WebParam(name = "arg2", partName = "arg2")
        String arg2)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void updateGroup(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1,
        @WebParam(name = "arg2", partName = "arg2")
        String arg2)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void updateProfile(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1,
        @WebParam(name = "arg2", partName = "arg2")
        String arg2,
        @WebParam(name = "arg3", partName = "arg3")
        int arg3)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void updateProfileLastLoginDate(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MembershipServiceException_Exception
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @throws MembershipServiceException_Exception
     */
    @WebMethod
    public void updateProfileOnlineStatus(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        int arg1)
        throws MembershipServiceException_Exception
    ;

}
package com.Service;

import com.DAO.MemberDAO;
import com.model.Member;
import com.utilities.DatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private MemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAO();
    }

    private String hashPassword(String password) {
        return password; // TODO: Implement actual password hashing
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return plainPassword.equals(hashedPassword);
    }

    public boolean registerMember(Member member) {
        try {
            if (member.getUsername() == null || member.getUsername().trim().isEmpty() ||
                    member.getPassword() == null || member.getPassword().trim().isEmpty() ||
                    member.getEmail() == null || member.getEmail().trim().isEmpty() ||
                    member.getIdMember() == null || member.getIdMember().trim().isEmpty()) {
                System.err.println("Validation failed: required fields or ID are empty.");
                return false;
            }

            if (memberDAO.findMemberByUsernameOrEmail(member.getUsername()) != null) {
                System.err.println("Registration failed: Username already exists.");
                return false;
            }
            if (memberDAO.findMemberByUsernameOrEmail(member.getEmail()) != null) {
                System.err.println("Registration failed: Email already exists.");
                return false;
            }

            member.setPassword(hashPassword(member.getPassword()));

            return memberDAO.insertMember(member);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Member loginMember(String identifier, String password) {
        try {
            Member member = memberDAO.findMemberByUsernameOrEmail(identifier);
            if (member != null && verifyPassword(password, member.getPassword())) {
                return member;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateMemberProfile(Member member) {
        try {
            return memberDAO.updateMember(member);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Member getMemberById(String memberId) {
        try {
            return memberDAO.findMemberById(memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Member> getAllMembers() {
        try {
            return memberDAO.getAllMembers();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean deleteMember(String memberId) {
        try {
            return memberDAO.deleteMember(memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMemberStatus(String memberId, String status) {
        try {
            Member member = memberDAO.findMemberById(memberId);
            if (member != null) {
                member.setUserStatus(Member.MemberStatus.valueOf(status.toUpperCase()));
                return memberDAO.updateMember(member);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Member> getFilteredAndSearchedMembers(String searchQuery, String statusFilter) {
        try {
            // Pass the search query and status filter to the DAO
            // The DAO will handle constructing the appropriate SQL query
            if (statusFilter != null && !statusFilter.equalsIgnoreCase("All")) {
                Member.MemberStatus statusEnum = Member.MemberStatus.valueOf(statusFilter.toUpperCase());
                return memberDAO.getMembersBySearchAndStatus(searchQuery, statusEnum);
            } else {
                return memberDAO.getMembersBySearchAndStatus(searchQuery, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

// tolong sesuain sama form data yang di view , ok

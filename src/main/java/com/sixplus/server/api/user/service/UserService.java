package com.sixplus.server.api.user.service;

import com.sixplus.server.api.user.domain.TB_QUEUE;
import com.sixplus.server.api.user.repository.QueueRepository;
import com.sixplus.server.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QueueRepository queueRepository;

    public String registerUser(String id, String password, String gender, String userName, String displayName, String email, String phone, String avatar) {
        if (userRepository.existsById(id)) {
            return "ȸ������ ����: �̹� �����ϴ� ������Դϴ�.";
        }

        if (queueRepository.existsById(id)) {
            long queuePosition = queueRepository.countByGender(gender) + 1;
            return String.format("����� %s ��⿭ %d ��° ��⿭�� ��ġ�մϴ�", gender.equals("male") ? "����" : "����", queuePosition);
        }

        // Add to queue
        TB_QUEUE newQueueEntry = new TB_QUEUE();
        newQueueEntry.setId(id);
        newQueueEntry.setPassword(password);
        newQueueEntry.setGender(gender);
        queueRepository.save(newQueueEntry);

        long queuePosition = queueRepository.countByGender(gender);
        return String.format("����� %s ��⿭ %d ��° ��⿭�� ��ġ�մϴ�", gender.equals("male") ? "����" : "����", queuePosition);
    }
}
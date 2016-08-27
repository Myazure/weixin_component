package org.myazure.weixin.service.currentUser;

import org.myazure.weixin.domain.MaRole;
import org.myazure.weixin.domain.CurrentUser;

/**
 * @author csyangchsh@gmail.com
 */
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        return currentUser != null
                && (currentUser.getRole() == MaRole.ADMIN || currentUser.getId().equals(userId));
    }
}

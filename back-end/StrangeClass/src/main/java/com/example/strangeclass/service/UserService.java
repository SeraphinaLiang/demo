package com.example.strangeclass.service;

import com.example.strangeclass.dao.UserRepository;
import com.example.strangeclass.entity.TicAccount;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.vo.*;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String ADMINISTRATOR = "Admin";

    private static final String IMAGE_PREFIX = "/resources/userImage/";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicAccountService ticAccountService;

    /**
     * 用户注册
     *
     * @param registerVo
     * @return
     */
    public void addUser(RegisterVo registerVo) throws Exception {
        if (userRepository.findByName(registerVo.getName()) != null) {
            throw new Exception("用户名已被注册");
        }
        User user = new User();
        BeanUtils.copyProperties(registerVo, user);
        user.setStatus(0);
        user.createTime();
        userRepository.save(user);
    }

    /**
     * 用户登录
     *
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    public ReplyLoginVo login(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = loginVo.getName();
        String password = loginVo.getPassword();

        User userDB = userRepository.findByNameAndPassword(name, password);
        if (userDB == null) {
            throw new Exception("用户名或密码错误");
        }
        if (userDB.getStatus() == 1) {
            throw new Exception("该用户已登录");
        }

        TicAccount ticAccount = ticAccountService.getAvailableAccount(name);
        if (ticAccount == null) {
            throw new Exception("SDK账号无法分配");
        }

        userDB.setStatus(1);
        userDB.updateTime();
        userRepository.save(userDB);

        userDB.setPassword("");

        HttpSession session = request.getSession();
        session.setAttribute("user", userDB);
        session.setMaxInactiveInterval(3600);
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        ReplyLoginVo replyLoginVo = new ReplyLoginVo();
        BeanUtils.copyProperties(userDB, replyLoginVo);
        replyLoginVo.setSdkId(ticAccount.getUserId());
        replyLoginVo.setSdkToken(ticAccount.getUserToken());

        return replyLoginVo;
    }

    /**
     * 用户登出
     *
     * @param session
     * @return
     */
    public void logout(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        String name = user.getName();
        User userDB = userRepository.findByName(name);

        if (userDB == null) {
            throw new Exception("用户名不存在");
        }
        if (userDB.getStatus() == 0) {
            throw new Exception("该用户已登出");
        }

        ticAccountService.returnAccount(name);

        userDB.setStatus(0);
        userDB.updateTime();
        userRepository.save(userDB);

        session.removeAttribute("user");
    }


    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * 查看任意用户信息
     *
     * @param userIdVo
     * @return
     */
    public UserVo viewInformation(UserIdVo userIdVo) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userIdVo.getId());
        if (optionalUser.isPresent()) {
            User userDB = optionalUser.get();
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userDB, userVo);
            return userVo;
        } else {
            throw new Exception("该用户不存在");
        }
    }

    /**
     * 用户密码修改
     *
     * @param userName
     * @param passwordVo
     * @return
     */
    public void changePassword(String userName, PasswordVo passwordVo) throws Exception {
        User userDB = userRepository.findByName(userName);
        if (!userDB.getPassword().equals(passwordVo.getOldPassword())) {
            throw new Exception("旧密码错误");
        }
        userDB.setPassword(passwordVo.getNewPassword());
        userRepository.save(userDB);
    }

    /**
     * 用户信息修改
     *
     * @param userInformationVo
     * @param request
     * @return
     */
    public UserVo changeInformation(UserInformationVo userInformationVo, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        User userDB = userRepository.findByName(user.getName());

        if (userInformationVo.getName() != null) {
            if (!userDB.getName().equals(userInformationVo.getName())) {
                if (userRepository.findByName(userInformationVo.getName()) != null) {
                    throw new Exception("用户名已存在");
                } else {
                    request.getSession().removeAttribute("user");
                    userDB.setName(userInformationVo.getName());
                }
            }
        }
        if (userInformationVo.getImage() != null) {
            userDB.setImage(userInformationVo.getImage());
        }
        if (userInformationVo.getInfo() != null) {
            userDB.setInfo(userInformationVo.getInfo());
        }
        user.setPassword("");
        request.getSession().setAttribute("user", userDB);
        userRepository.save(userDB);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDB, userVo);

        return userVo;
    }


    public void save(User userDB) {
        userRepository.save(userDB);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);

    }

    public UserImageVo handleUploadImage(MultipartFile imageFile, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        String returnStr = null;
        if (!imageFile.isEmpty()) {
            File userImage = new File(request.getSession().getServletContext().getRealPath(IMAGE_PREFIX));
            File[] userImageList = userImage.listFiles();
            if (userImageList != null) {
                for (File file : userImageList) {
                    String imageName = file.getName();
                    String userName = imageName.split("_")[0];
                    if (userName.equals(user.getName())) {
                        file.delete();
                        break;
                    }
                }
            }
            String imageName = user.getName() + "_" + new Date().hashCode() + ".jpg";
            String path = request.getSession().getServletContext().getRealPath(IMAGE_PREFIX) + "/" + imageName;
            File localImageFile = new File(path);
            try {
                File localImagePath = localImageFile.getParentFile();
                if (!localImagePath.exists()) {
                    localImagePath.mkdirs();
                }
                //上传图片到目录
                System.out.println(path);
                byte[] bytes = imageFile.getBytes();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localImageFile));
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.close();
                User userDB = userRepository.findByName(user.getName());
                returnStr = request.getContextPath() + IMAGE_PREFIX + imageName;
                userDB.setImage(returnStr);
                userRepository.save(userDB);

            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("上传失败");
            }
        }
        return new UserImageVo(returnStr);
    }

    /**
     * 管理员查看所有用户信息
     *
     * @return
     */
    public UserListVoForAdmin viewAllInformationForAdmin() throws Exception {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            if (user.getRole().equals(ADMINISTRATOR)) {
                userList.remove(user);
                break;
            }
        }
        UserListVoForAdmin userListVoForAdmin = new UserListVoForAdmin();
        userListVoForAdmin.setUserList(userList);
        return userListVoForAdmin;
    }
}

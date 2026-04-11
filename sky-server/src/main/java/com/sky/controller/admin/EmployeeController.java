package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关操作")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("Employee login: {}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Create employee: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询员工")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
            log.info("进行员工分页查询: {}", employeePageQueryDTO);
            PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
            return Result.success(pageResult);
    }

    /**
     * 员工的启用和禁用
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value="员工的启用禁用" )
    public Result startAndStop(@PathVariable Integer status,Long id) {
        log.info("对员工进行启用和禁用,{},{}",status,id);
        employeeService.starAndStop(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询员工")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }


    @PutMapping
    @ApiOperation(value = "修改员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("对员工信息进行修改，{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
